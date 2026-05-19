package com.youpin.quickreply.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.youpin.quickreply.R;
import com.youpin.quickreply.adapter.PhraseAdapter;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Phrase;
import com.youpin.quickreply.service.FloatingWindowService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    
    private static final int REQUEST_OVERLAY_PERMISSION = 1001;
    private static final int REQUEST_STORAGE_PERMISSION = 1002;
    
    private RecyclerView recyclerView;
    private PhraseAdapter adapter;
    private TabLayout tabLayout;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;
    private BottomNavigationView bottomNavigation;
    
    private ExecutorService executor;
    private String currentType = "company";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        executor = Executors.newSingleThreadExecutor();
        
        initViews();
        initRecyclerView();
        initTabs();
        initBottomNavigation();
        checkPermissions();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        tabLayout = findViewById(R.id.tab_layout);
        etSearch = findViewById(R.id.et_search);
        tvEmpty = findViewById(R.id.tv_empty);
        fabAdd = findViewById(R.id.fab_add);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhraseEditActivity.class);
            intent.putExtra("type", currentType);
            startActivity(intent);
        });
        
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            searchPhrases(etSearch.getText().toString().trim());
            return true;
        });
    }
    
    private void initRecyclerView() {
        adapter = new PhraseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        adapter.setOnPhraseClickListener(phrase -> {
            // 复制话术
            android.content.ClipboardManager clipboard = 
                (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("phrase", phrase.getContent());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            
            // 增加使用次数
            executor.execute(() -> {
                AppDatabase.getInstance(this).phraseDao().incrementUseCount(phrase.getId());
            });
        });
        
        adapter.setOnPhraseLongClickListener(phrase -> {
            showPhraseOptions(phrase);
        });
    }
    
    private void initTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("公司话术"));
        tabLayout.addTab(tabLayout.newTab().setText("小组话术"));
        tabLayout.addTab(tabLayout.newTab().setText("私人话术"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentType = "company";
                        break;
                    case 1:
                        currentType = "team";
                        break;
                    case 2:
                        currentType = "private";
                        break;
                }
                loadPhrases();
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void initBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_phrases) {
                return true;
            } else if (itemId == R.id.nav_team) {
                Toast.makeText(this, "团队功能开发中", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
    
    private void loadPhrases() {
        executor.execute(() -> {
            List<Phrase> phrases = AppDatabase.getInstance(this).phraseDao()
                .getPhrasesByTypeSync(currentType);
            runOnUiThread(() -> {
                adapter.setPhrases(phrases);
                tvEmpty.setVisibility(phrases.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }
    
    private void searchPhrases(String keyword) {
        if (keyword.isEmpty()) {
            loadPhrases();
            return;
        }
        
        executor.execute(() -> {
            List<Phrase> phrases = AppDatabase.getInstance(this).phraseDao()
                .searchPhrasesSync(keyword);
            runOnUiThread(() -> {
                adapter.setPhrases(phrases);
                tvEmpty.setVisibility(phrases.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }
    
    private void showPhraseOptions(Phrase phrase) {
        new AlertDialog.Builder(this)
            .setTitle(phrase.getTitle())
            .setItems(new String[]{"编辑", "删除"}, (dialog, which) -> {
                if (which == 0) {
                    Intent intent = new Intent(this, PhraseEditActivity.class);
                    intent.putExtra("phrase_id", phrase.getId());
                    startActivity(intent);
                } else if (which == 1) {
                    deletePhrase(phrase);
                }
            })
            .show();
    }
    
    private void deletePhrase(Phrase phrase) {
        new AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除这条话术吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                executor.execute(() -> {
                    AppDatabase.getInstance(this).phraseDao().delete(phrase);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                        loadPhrases();
                    });
                });
            })
            .setNegativeButton("取消", null)
            .show();
    }
    
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
            } else {
                startFloatingService();
            }
        } else {
            startFloatingService();
        }
    }
    
    private void startFloatingService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, FloatingWindowService.class));
        } else {
            startService(new Intent(this, FloatingWindowService.class));
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startFloatingService();
                }
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadPhrases();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}