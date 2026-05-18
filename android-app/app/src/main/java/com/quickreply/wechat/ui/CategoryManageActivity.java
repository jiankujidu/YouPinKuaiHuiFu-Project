package com.quickreply.wechat.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quickreply.wechat.R;
import com.quickreply.wechat.adapter.CategoryAdapter;
import com.quickreply.wechat.database.AppDatabase;
import com.quickreply.wechat.model.Category;

public class CategoryManageActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private EditText etCategoryName;
    private RadioGroup rgType;
    private Button btnAdd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manage);
        
        initToolbar();
        initViews();
        initRecyclerView();
    }
    
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("分类管理");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void initViews() {
        etCategoryName = findViewById(R.id.et_category_name);
        rgType = findViewById(R.id.rg_type);
        btnAdd = findViewById(R.id.btn_add);
        recyclerView = findViewById(R.id.recycler_view);
        
        btnAdd.setOnClickListener(v -> addCategory());
    }
    
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(this);
        recyclerView.setAdapter(adapter);
        
        adapter.setOnItemClickListener(category -> {
            // 编辑分类
        });
        
        adapter.setOnDeleteClickListener(category -> {
            deleteCategory(category);
        });
        
        loadCategories();
    }
    
    private void loadCategories() {
        AppDatabase.getInstance(this).categoryDao().getAllCategories().observe(this, categories -> {
            adapter.setCategories(categories);
        });
    }
    
    private void addCategory() {
        String name = etCategoryName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入分类名称", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String type;
        int checkedId = rgType.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_company) {
            type = Category.TYPE_COMPANY;
        } else if (checkedId == R.id.rb_team) {
            type = Category.TYPE_TEAM;
        } else {
            type = Category.TYPE_PRIVATE;
        }
        
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setColor(getColorForType(type));
        
        new Thread(() -> {
            AppDatabase.getInstance(this).categoryDao().insert(category);
            runOnUiThread(() -> {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                etCategoryName.setText("");
            });
        }).start();
    }
    
    private void deleteCategory(Category category) {
        new Thread(() -> {
            AppDatabase.getInstance(this).categoryDao().delete(category);
            runOnUiThread(() -> Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show());
        }).start();
    }
    
    private int getColorForType(String type) {
        switch (type) {
            case Category.TYPE_COMPANY:
                return 0xFF07C160; // 微信绿
            case Category.TYPE_TEAM:
                return 0xFF10AEFF; // 蓝色
            case Category.TYPE_PRIVATE:
                return 0xFFFF9500; // 橙色
            default:
                return 0xFF999999;
        }
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
