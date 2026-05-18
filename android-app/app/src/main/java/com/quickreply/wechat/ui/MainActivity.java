package com.quickreply.wechat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.quickreply.wechat.R;
import com.quickreply.wechat.adapter.PhraseAdapter;
import com.quickreply.wechat.database.AppDatabase;
import com.quickreply.wechat.model.Phrase;
import com.quickreply.wechat.service.FloatingWindowService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private PhraseAdapter adapter;
    private TabLayout tabLayout;
    private FloatingActionButton fabAdd;
    private TextView emptyView;
    
    private String currentType = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        initToolbar();
        initRecyclerView();
        initTabs();
        initFab();
        
        checkOverlayPermission();
        startFloatingService();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        tabLayout = findViewById(R.id.tab_layout);
        fabAdd = findViewById(R.id.fab_add);
        emptyView = findViewById(R.id.empty_view);
    }
    
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("快捷回复");
        }
    }
    
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhraseAdapter(this);
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(new PhraseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Phrase phrase) {
                editPhrase(phrase);
            }
            
            @Override
            public void onItemLongClick(Phrase phrase) {
                showDeleteDialog(phrase);
            }
            
            @Override
            public void onCopyClick(Phrase phrase) {
                copyToClipboard(phrase);
            }
        });
        
        loadPhrases();
    }
    
    private void initTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setText("公司话术"));
        tabLayout.addTab(tabLayout.newTab().setText("小组话术"));
        tabLayout.addTab(tabLayout.newTab().setText("私人话术"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentType = null;
                        break;
                    case 1:
                        currentType = "company";
                        break;
                    case 2:
                        currentType = "team";
                        break;
                    case 3:
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
    
    private void initFab() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhraseEditActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadPhrases() {
        AppDatabase.getInstance(this).phraseDao().getAllPhrases().observe(this, phrases -> {
            List<Phrase> filteredPhrases = new ArrayList<>();
            if (phrases != null) {
                if (currentType == null) {
                    filteredPhrases.addAll(phrases);
                } else {
                    for (Phrase phrase : phrases) {
                        if (currentType.equals(phrase.getCategoryType())) {
                            filteredPhrases.add(phrase);
                        }
                    }
                }
            }
            
            adapter.setPhrases(filteredPhrases);
            
            if (filteredPhrases.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
    
    private void editPhrase(Phrase phrase) {
        Intent intent = new Intent(this, PhraseEditActivity.class);
        intent.putExtra("phrase", phrase);
        startActivity(intent);
    }
    
    private void showDeleteDialog(Phrase phrase) {
        new AlertDialog.Builder(this)
                .setTitle("删除话术")
                .setMessage("确定要删除这条话术吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    deletePhrase(phrase);
                })
                .setNegativeButton("取消", null)
                .show();
    }
    
    private void deletePhrase(Phrase phrase) {
        new Thread(() -> {
            AppDatabase.getInstance(this).phraseDao().delete(phrase);
            runOnUiThread(() -> Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show());
        }).start();
    }
    
    private void copyToClipboard(Phrase phrase) {
        android.content.ClipboardManager clipboard = 
                (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("phrase", phrase.getContent());
        clipboard.setPrimaryClip(clip);
        
        // 更新使用次数
        new Thread(() -> {
            AppDatabase.getInstance(this).phraseDao()
                    .incrementUseCount(phrase.getId(), System.currentTimeMillis());
        }).start();
        
        Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }
    
    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                new AlertDialog.Builder(this)
                        .setTitle("需要悬浮窗权限")
                        .setMessage("请在设置中开启悬浮窗权限，以便使用快捷回复功能")
                        .setPositiveButton("去开启", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        }
    }
    
    private void startFloatingService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            return;
        }
        Intent intent = new Intent(this, FloatingWindowService.class);
        startService(intent);
    }
}