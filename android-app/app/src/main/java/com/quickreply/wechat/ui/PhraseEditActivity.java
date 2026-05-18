package com.quickreply.wechat.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.quickreply.wechat.R;
import com.quickreply.wechat.database.AppDatabase;
import com.quickreply.wechat.model.Category;
import com.quickreply.wechat.model.Phrase;

import java.util.ArrayList;
import java.util.List;

public class PhraseEditActivity extends AppCompatActivity {
    
    private EditText etContent;
    private Spinner spinnerCategory;
    private Button btnSave;
    
    private Phrase phrase;
    private List<Category> categories = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_edit);
        
        initToolbar();
        initViews();
        loadCategories();
        
        // 检查是否是编辑模式
        phrase = (Phrase) getIntent().getSerializableExtra("phrase");
        if (phrase != null) {
            etContent.setText(phrase.getContent());
            setTitle("编辑话术");
        } else {
            phrase = new Phrase();
            setTitle("新建话术");
        }
    }
    
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void initViews() {
        etContent = findViewById(R.id.et_content);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnSave = findViewById(R.id.btn_save);
        
        btnSave.setOnClickListener(v -> savePhrase());
    }
    
    private void loadCategories() {
        AppDatabase.getInstance(this).categoryDao().getAllCategories().observe(this, cats -> {
            categories.clear();
            if (cats != null) {
                categories.addAll(cats);
            }
            
            // 添加默认分类
            if (categories.isEmpty()) {
                createDefaultCategories();
                return;
            }
            
            List<String> categoryNames = new ArrayList<>();
            for (Category cat : categories) {
                categoryNames.add(cat.getName());
            }
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                    android.R.layout.simple_spinner_item, categoryNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapter);
            
            // 设置当前选中的分类
            if (phrase.getCategoryId() != null) {
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId().equals(phrase.getCategoryId())) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }
        });
    }
    
    private void createDefaultCategories() {
        new Thread(() -> {
            // 公司话术
            Category company = new Category();
            company.setName("常用回复");
            company.setType(Category.TYPE_COMPANY);
            company.setColor(0xFF07C160);
            AppDatabase.getInstance(this).categoryDao().insert(company);
            
            // 小组话术
            Category team = new Category();
            team.setName("团队话术");
            team.setType(Category.TYPE_TEAM);
            team.setColor(0xFF10AEFF);
            AppDatabase.getInstance(this).categoryDao().insert(team);
            
            // 私人话术
            Category privateCat = new Category();
            privateCat.setName("我的收藏");
            privateCat.setType(Category.TYPE_PRIVATE);
            privateCat.setColor(0xFFFF9500);
            AppDatabase.getInstance(this).categoryDao().insert(privateCat);
            
            runOnUiThread(() -> loadCategories());
        }).start();
    }
    
    private void savePhrase() {
        String content = etContent.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入话术内容", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int position = spinnerCategory.getSelectedItemPosition();
        if (position < 0 || position >= categories.size()) {
            Toast.makeText(this, "请选择分类", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Category category = categories.get(position);
        
        phrase.setContent(content);
        phrase.setCategoryId(category.getId());
        phrase.setCategoryName(category.getName());
        phrase.setCategoryType(category.getType());
        phrase.setColor(category.getColor());
        phrase.setUpdateTime(System.currentTimeMillis());
        
        new Thread(() -> {
            AppDatabase.getInstance(this).phraseDao().insert(phrase);
            runOnUiThread(() -> {
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
