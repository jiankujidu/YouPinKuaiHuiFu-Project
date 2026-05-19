package com.youpin.quickreply.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.youpin.quickreply.R;
import com.youpin.quickreply.adapter.CategoryPhraseAdapter;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Category;
import com.youpin.quickreply.model.Phrase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TeamActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private CategoryPhraseAdapter adapter;
    private TextView tvEmpty;
    private EditText etSearch;
    private ImageButton btnSearch;
    private FloatingActionButton fabAdd;
    
    private ExecutorService executor;
    private List<Category> categories = new ArrayList<>();
    private List<Phrase> phrases = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        
        executor = Executors.newSingleThreadExecutor();
        
        initViews();
        setupListeners();
        loadData();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        tvEmpty = findViewById(R.id.tv_empty);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        fabAdd = findViewById(R.id.fab_add);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryPhraseAdapter();
        recyclerView.setAdapter(adapter);
        
        // 设置点击监听
        adapter.setOnPhraseClickListener(new CategoryPhraseAdapter.OnPhraseClickListener() {
            @Override
            public void onPhraseClick(Phrase phrase) {
                // 复制到剪贴板
                android.content.ClipboardManager clipboard = 
                    (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("phrase", phrase.getContent());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(TeamActivity.this, "已复制: " + phrase.getTitle(), Toast.LENGTH_SHORT).show();
                
                // 增加使用次数
                incrementUsage(phrase);
            }
            
            @Override
            public void onPhraseLongClick(Phrase phrase) {
                showPhraseOptions(phrase);
            }
        });
    }
    
    private void setupListeners() {
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            searchPhrases(keyword);
        });
        
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhraseEditActivity.class);
            intent.putExtra("type", "team");
            startActivity(intent);
        });
        
        // 分类管理按钮
        findViewById(R.id.btn_manage_categories).setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoryManageActivity.class);
            intent.putExtra("type", "team");
            startActivity(intent);
        });
    }
    
    private void loadData() {
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(this);
                
                // 加载团队话术的分类
                categories = db.categoryDao().getCategoriesByType("team");
                
                // 加载团队话术
                phrases = db.phraseDao().getPhrasesByType("team");
                
                runOnUiThread(() -> {
                    adapter.setData(categories, phrases);
                    tvEmpty.setVisibility(phrases.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void searchPhrases(String keyword) {
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        executor.execute(() -> {
            try {
                List<Phrase> searchResults = AppDatabase.getInstance(this).phraseDao()
                    .searchPhrasesByType("team", keyword);
                
                runOnUiThread(() -> {
                    adapter.setData(new ArrayList<>(), searchResults);
                    tvEmpty.setVisibility(searchResults.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void incrementUsage(Phrase phrase) {
        executor.execute(() -> {
            try {
                AppDatabase.getInstance(this).phraseDao().incrementUseCount(phrase.getId());
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
                    // 编辑
                    Intent intent = new Intent(this, PhraseEditActivity.class);
                    intent.putExtra("phrase_id", phrase.getId());
                    startActivity(intent);
                } else if (which == 1) {
                    // 删除
                    deletePhrase(phrase);
                }
            })
            .show();
    }
    
    private void deletePhrase(Phrase phrase) {
        new AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除话术 \"" + phrase.getTitle() + "\" 吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                executor.execute(() -> {
                    try {
                        AppDatabase.getInstance(this).phraseDao().delete(phrase);
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
    
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
