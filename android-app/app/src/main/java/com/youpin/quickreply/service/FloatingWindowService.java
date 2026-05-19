package com.youpin.quickreply.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.youpin.quickreply.R;
import com.youpin.quickreply.adapter.FloatingPhraseAdapter;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FloatingWindowService extends Service {
    
    private WindowManager windowManager;
    private View floatingView;
    private View expandedView;
    private ImageView floatingBall;
    private LinearLayout layoutExpanded;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FloatingPhraseAdapter adapter;
    
    private WindowManager.LayoutParams params;
    private WindowManager.LayoutParams expandedParams;
    
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    
    private boolean isExpanded = false;
    private ExecutorService executor;
    
    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        executor = Executors.newSingleThreadExecutor();
        createFloatingWindow();
    }
    
    private void createFloatingWindow() {
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_ball, null);
        floatingBall = floatingView.findViewById(R.id.floating_ball);
        
        params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O 
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY 
                : WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = getScreenWidth() - 150;
        params.y = getScreenHeight() / 2;
        
        windowManager.addView(floatingView, params);
        
        expandedView = LayoutInflater.from(this).inflate(R.layout.layout_floating_window, null);
        layoutExpanded = expandedView.findViewById(R.id.layout_expanded);
        recyclerView = expandedView.findViewById(R.id.recycler_view);
        searchView = expandedView.findViewById(R.id.search_view);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FloatingPhraseAdapter();
        recyclerView.setAdapter(adapter);
        
        adapter.setOnPhraseClickListener(phrase -> {
            copyToClipboard(phrase.getContent());
            Toast.makeText(this, "已复制: " + phrase.getTitle(), Toast.LENGTH_SHORT).show();
            incrementUsageCount(phrase);
            collapseWindow();
        });
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                searchPhrases(newText);
                return true;
            }
        });
        
        floatingBall.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                    
                case MotionEvent.ACTION_MOVE:
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(floatingView, params);
                    return true;
                    
                case MotionEvent.ACTION_UP:
                    // 判断是点击还是拖动
                    float deltaX = event.getRawX() - initialTouchX;
                    float deltaY = event.getRawY() - initialTouchY;
                    if (Math.abs(deltaX) < 10 && Math.abs(deltaY) < 10) {
                        // 点击事件
                        if (isExpanded) {
                            collapseWindow();
                        } else {
                            expandWindow();
                        }
                    } else {
                        // 拖动结束，吸附到边缘
                        snapToEdge();
                    }
                    return true;
            }
            return false;
        });
        
        loadPhrases();
    }
    
    private void expandWindow() {
        if (expandedView.getParent() == null) {
            expandedParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O 
                    ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY 
                    : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            );
            expandedParams.gravity = Gravity.BOTTOM;
            expandedParams.y = 200;
            
            windowManager.addView(expandedView, expandedParams);
        }
        layoutExpanded.setVisibility(View.VISIBLE);
        isExpanded = true;
        loadPhrases();
    }
    
    private void collapseWindow() {
        if (layoutExpanded != null) {
            layoutExpanded.setVisibility(View.GONE);
        }
        isExpanded = false;
    }
    
    private void snapToEdge() {
        int screenWidth = getScreenWidth();
        int centerX = params.x + floatingView.getWidth() / 2;
        
        if (centerX < screenWidth / 2) {
            params.x = 0;
        } else {
            params.x = screenWidth - floatingView.getWidth();
        }
        
        windowManager.updateViewLayout(floatingView, params);
    }
    
    private void loadPhrases() {
        executor.execute(() -> {
            try {
                List<Phrase> phrases = AppDatabase.getInstance(this).phraseDao()
                    .getPhrasesByType("private");
                if (phrases == null || phrases.isEmpty()) {
                    phrases = AppDatabase.getInstance(this).phraseDao()
                        .getPhrasesByType("company");
                }
                if (phrases == null) {
                    phrases = new ArrayList<>();
                }
                final List<Phrase> finalPhrases = phrases;
                if (recyclerView != null) {
                    recyclerView.post(() -> {
                        if (adapter != null) {
                            adapter.setPhrases(finalPhrases);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void searchPhrases(String keyword) {
        executor.execute(() -> {
            try {
                List<Phrase> phrases;
                if (keyword == null || keyword.isEmpty()) {
                    phrases = AppDatabase.getInstance(this).phraseDao()
                        .getPhrasesByType("private");
                } else {
                    phrases = AppDatabase.getInstance(this).phraseDao()
                        .searchPhrases(keyword);
                }
                if (phrases == null) {
                    phrases = new ArrayList<>();
                }
                final List<Phrase> finalPhrases = phrases;
                if (recyclerView != null) {
                    recyclerView.post(() -> {
                        if (adapter != null) {
                            adapter.setPhrases(finalPhrases);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("phrase", text);
        clipboard.setPrimaryClip(clip);
    }
    
    private void incrementUsageCount(Phrase phrase) {
        executor.execute(() -> {
            AppDatabase.getInstance(this).phraseDao().incrementUseCount(phrase.getId());
        });
    }
    
    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
    
    private int getScreenHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
        if (expandedView != null) {
            windowManager.removeView(expandedView);
        }
        executor.shutdown();
    }
}