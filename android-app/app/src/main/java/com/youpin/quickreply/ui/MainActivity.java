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
import com.youpin.quickreply.adapter.CategoryPhraseAdapter;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Category;
import com.youpin.quickreply.model.Phrase;
import com.youpin.quickreply.service.FloatingWindowService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    
    private static final int REQUEST_OVERLAY_PERMISSION = 1001;
    
    private RecyclerView recyclerView;
    private CategoryPhraseAdapter adapter;
    private TabLayout tabLayout;
    private EditText etSearch;
    private TextView tvEmpty;
    private FloatingActionButton fabAdd;
    private BottomNavigationView bottomNavigation;
    
    private ExecutorService executor;
    private String currentType = "company";
    private List<Category> categories = new ArrayList<>();
    private List<Phrase> phrases = new ArrayList<>();
    
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
        
        // 延迟加载数据，确保数据库初始化完成
        recyclerView.postDelayed(() -> loadData(), 500);
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
        adapter = new CategoryPhraseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        adapter.setOnPhraseClickListener(new CategoryPhraseAdapter.OnPhraseClickListener() {
            @Override
            public void onPhraseClick(Phrase phrase) {
                // 复制话术
                android.content.ClipboardManager clipboard = 
                    (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("phrase", phrase.getContent());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                
                // 增加使用次数
                executor.execute(() -> {
                    try {
                        AppDatabase.getInstance(MainActivity.this).phraseDao().incrementUseCount(phrase.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            
            @Override
            public void onPhraseLongClick(Phrase phrase) {
                showPhraseOptions(phrase);
            }
        });
        
        adapter.setOnCategoryClickListener(new CategoryPhraseAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                // 切换一级分类展开状态
                category.setExpanded(!category.isExpanded());
                refreshAdapter();
            }
            
            @Override
            public void onCategoryExpandClick(Category category) {
                // 切换二级分类展开状态
                category.setExpanded(!category.isExpanded());
                refreshAdapter();
            }
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
                loadData();
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
    
    private void loadData() {
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(this);
                
                // 加载分类
                categories = db.categoryDao().getAllCategories(currentType);
                
                // 计算每个分类的话术数量
                for (Category cat : categories) {
                    if (cat.getParentId() == null) {
                        // 一级分类数量 = 所有子分类数量之和
                        int count = db.categoryDao().getPhraseCountByParentCategory(cat.getId());
                        cat.setPhraseCount(count);
                    } else {
                        // 二级分类数量
                        int count = db.categoryDao().getPhraseCountByCategory(cat.getId());
                        cat.setPhraseCount(count);
                    }
                }
                
                // 加载话术
                phrases = db.phraseDao().getPhrasesByType(currentType);
                
                runOnUiThread(() -> {
                    refreshAdapter();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "加载数据失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private void refreshAdapter() {
        adapter.setData(categories, phrases);
        tvEmpty.setVisibility(categories.isEmpty() && phrases.isEmpty() ? View.VISIBLE : View.GONE);
    }
    
    private void searchPhrases(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            loadData();
            return;
        }
        
        executor.execute(() -> {
            try {
                List<Phrase> searchResults = AppDatabase.getInstance(this).phraseDao()
                    .searchPhrasesByType(currentType, keyword);
                if (searchResults == null) {
                    searchResults = new ArrayList<>();
                }
                final List<Phrase> finalResults = searchResults;
                
                runOnUiThread(() -> {
                    // 搜索时只显示话术，不显示分类结构
                    adapter.setData(new ArrayList<>(), finalResults);
                    tvEmpty.setVisibility(finalResults.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            .setMessage("确定要删除话术\"" + phrase.getTitle() + "\"吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                executor.execute(() -> {
                    try {
                        AppDatabase.getInstance(this).phraseDao().deleteById(phrase.getId());
                        runOnUiThread(() -> {
                            Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                            loadData();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        loadData();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}
