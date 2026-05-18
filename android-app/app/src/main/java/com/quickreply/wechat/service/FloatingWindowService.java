package com.quickreply.wechat.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quickreply.wechat.R;
import com.quickreply.wechat.adapter.FloatingPhraseAdapter;
import com.quickreply.wechat.database.AppDatabase;
import com.quickreply.wechat.model.Phrase;
import com.quickreply.wechat.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class FloatingWindowService extends Service {
    
    private static final String CHANNEL_ID = "floating_service_channel";
    private static final int NOTIFICATION_ID = 1;
    
    private WindowManager windowManager;
    private View floatingView;
    private View expandedView;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private FloatingPhraseAdapter adapter;
    
    private WindowManager.LayoutParams params;
    private WindowManager.LayoutParams expandedParams;
    
    private boolean isExpanded = false;
    private Handler mainHandler;
    private Vibrator vibrator;
    
    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mainHandler = new Handler(Looper.getMainLooper());
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (floatingView == null) {
            createFloatingWindow();
        }
        return START_STICKY;
    }
    
    private void createFloatingWindow() {
        // 悬浮球
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_ball, null);
        ImageView floatingIcon = floatingView.findViewById(R.id.floating_icon);
        
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O 
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY 
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 200;
        
        windowManager.addView(floatingView, params);
        
        // 点击展开
        floatingView.setOnClickListener(v -> {
            vibrate();
            showExpandedWindow();
        });
        
        // 拖拽移动
        floatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float touchX, touchY;
            private long touchStartTime;
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        touchX = event.getRawX();
                        touchY = event.getRawY();
                        touchStartTime = System.currentTimeMillis();
                        return true;
                        
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - touchX);
                        params.y = initialY + (int) (event.getRawY() - touchY);
                        windowManager.updateViewLayout(floatingView, params);
                        return true;
                        
                    case MotionEvent.ACTION_UP:
                        long touchDuration = System.currentTimeMillis() - touchStartTime;
                        if (touchDuration < 200) {
                            v.performClick();
                        }
                        // 吸附边缘
                        snapToEdge();
                        return true;
                }
                return false;
            }
        });
    }
    
    private void showExpandedWindow() {
        if (isExpanded) return;
        
        isExpanded = true;
        floatingView.setVisibility(View.GONE);
        
        expandedView = LayoutInflater.from(this).inflate(R.layout.layout_floating_window, null);
        
        // 初始化视图
        recyclerView = expandedView.findViewById(R.id.recycler_view);
        searchInput = expandedView.findViewById(R.id.search_input);
        ImageButton searchBtn = expandedView.findViewById(R.id.search_btn);
        ImageButton closeBtn = expandedView.findViewById(R.id.close_btn);
        ImageButton settingsBtn = expandedView.findViewById(R.id.settings_btn);
        LinearLayout tabCompany = expandedView.findViewById(R.id.tab_company);
        LinearLayout tabTeam = expandedView.findViewById(R.id.tab_team);
        LinearLayout tabPrivate = expandedView.findViewById(R.id.tab_private);
        
        // 设置 RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FloatingPhraseAdapter(this);
        recyclerView.setAdapter(adapter);
        
        // 加载数据
        loadPhrases(null);
        
        // 设置点击事件
        adapter.setOnItemClickListener(phrase -> {
            copyToClipboard(phrase.getContent());
            incrementUseCount(phrase);
            Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
        });
        
        searchBtn.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();
            loadPhrases(keyword);
        });
        
        closeBtn.setOnClickListener(v -> hideExpandedWindow());
        
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        
        // Tab 切换
        tabCompany.setOnClickListener(v -> loadPhrasesByType("company"));
        tabTeam.setOnClickListener(v -> loadPhrasesByType("team"));
        tabPrivate.setOnClickListener(v -> loadPhrasesByType("private"));
        
        // 创建展开窗口
        expandedParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O 
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY 
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        expandedParams.gravity = Gravity.CENTER;
        
        windowManager.addView(expandedView, expandedParams);
        
        // 入场动画
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
        expandedView.startAnimation(slideIn);
    }
    
    private void hideExpandedWindow() {
        if (!isExpanded || expandedView == null) return;
        
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            
            @Override
            public void onAnimationEnd(Animation animation) {
                windowManager.removeView(expandedView);
                expandedView = null;
                isExpanded = false;
                floatingView.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        expandedView.startAnimation(slideOut);
    }
    
    private void snapToEdge() {
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        
        int centerX = params.x + floatingView.getWidth() / 2;
        if (centerX < screenWidth / 2) {
            params.x = 0;
        } else {
            params.x = screenWidth - floatingView.getWidth();
        }
        windowManager.updateViewLayout(floatingView, params);
    }
    
    private void loadPhrases(String keyword) {
        new Thread(() -> {
            List<Phrase> phrasesList;
            if (keyword == null || keyword.isEmpty()) {
                phrasesList = AppDatabase.getInstance(this).phraseDao().getAllPhrases().getValue();
            } else {
                phrasesList = AppDatabase.getInstance(this).phraseDao().searchPhrases(keyword).getValue();
            }
            if (phrasesList == null) phrasesList = new ArrayList<>();
            final List<Phrase> finalPhrases = phrasesList;
            
            mainHandler.post(() -> adapter.setPhrases(finalPhrases));
        }).start();
    }
    
    private void loadPhrasesByType(String type) {
        new Thread(() -> {
            List<Phrase> phrasesList = AppDatabase.getInstance(this).phraseDao()
                    .getPhrasesByType(type).getValue();
            if (phrasesList == null) phrasesList = new ArrayList<>();
            final List<Phrase> finalPhrases = phrasesList;
            
            mainHandler.post(() -> adapter.setPhrases(finalPhrases));
        }).start();
    }
    
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QuickReply", text);
        clipboard.setPrimaryClip(clip);
    }
    
    private void incrementUseCount(Phrase phrase) {
        new Thread(() -> {
            AppDatabase.getInstance(this).phraseDao()
                    .incrementUseCount(phrase.getId(), System.currentTimeMillis());
        }).start();
    }
    
    private void vibrate() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(50);
        }
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "悬浮窗服务",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("保持快捷回复悬浮窗运行");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    
    private Notification createNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("快捷回复运行中")
                .setContentText("点击打开管理界面")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
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
    }
}
