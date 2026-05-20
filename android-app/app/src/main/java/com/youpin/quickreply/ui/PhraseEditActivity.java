package com.youpin.quickreply.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.youpin.quickreply.R;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Category;
import com.youpin.quickreply.model.Phrase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhraseEditActivity extends AppCompatActivity {
    
    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_FILE_PICK = 1002;
    
    private RadioGroup rgType;
    private Spinner spinnerLevel1;
    private Spinner spinnerLevel2;
    private EditText etTitle;
    private EditText etContent;
    private Button btnSave;
    private Button btnSaveBottom;
    private ImageButton btnBack;
    
    private ExecutorService executor;
    private long phraseId = -1;
    private String defaultType = "company";
    
    private List<Category> level1Categories = new ArrayList<>();
    private List<Category> level2Categories = new ArrayList<>();
    private Category selectedLevel1 = null;
    private Category selectedLevel2 = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_edit_simple);
        
        executor = Executors.newSingleThreadExecutor();
        
        initViews();
        setupListeners();
        
        phraseId = getIntent().getLongExtra("phrase_id", -1);
        defaultType = getIntent().getStringExtra("type");
        if (defaultType == null) defaultType = "company";
        
        setTypeSelection(defaultType);
        loadCategories();
        
        if (phraseId != -1) {
            loadPhrase();
        }
    }
    
    private void initViews() {
        rgType = findViewById(R.id.rg_type);
        spinnerLevel1 = findViewById(R.id.spinner_level1);
        spinnerLevel2 = findViewById(R.id.spinner_level2);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        btnSave = findViewById(R.id.btn_save);
        btnSaveBottom = findViewById(R.id.btn_save_bottom);
        btnBack = findViewById(R.id.btn_back);
    }
    
    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> savePhrase());
        btnSaveBottom.setOnClickListener(v -> savePhrase());
        
        rgType.setOnCheckedChangeListener((group, checkedId) -> loadCategories());
        
        spinnerLevel1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position <= level1Categories.size()) {
                    selectedLevel1 = level1Categories.get(position - 1);
                    loadLevel2Categories(selectedLevel1.getId());
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        spinnerLevel2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position <= level2Categories.size()) {
                    selectedLevel2 = level2Categories.get(position - 1);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    
    private void setTypeSelection(String type) {
        switch (type) {
            case "company": rgType.check(R.id.rb_company); break;
            case "team": rgType.check(R.id.rb_team); break;
            default: rgType.check(R.id.rb_private); break;
        }
    }
    
    private String getSelectedType() {
        int checkedId = rgType.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_company) return "company";
        if (checkedId == R.id.rb_team) return "team";
        return "private";
    }
    
    private void loadCategories() {
        String type = getSelectedType();
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(this);
                level1Categories = db.categoryDao().getLevel1Categories(type);
                
                runOnUiThread(() -> {
                    List<String> level1Names = new ArrayList<>();
                    level1Names.add("请选择一级分类");
                    for (Category cat : level1Categories) level1Names.add(cat.getName());
                    
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, 
                        android.R.layout.simple_spinner_item, level1Names);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLevel1.setAdapter(adapter1);
                });
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
    
    private void loadLevel2Categories(long parentId) {
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(this);
                level2Categories = db.categoryDao().getLevel2Categories(parentId);
                
                runOnUiThread(() -> {
                    List<String> level2Names = new ArrayList<>();
                    level2Names.add("请选择二级分类");
                    for (Category cat : level2Categories) level2Names.add(cat.getName());
                    
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, level2Names);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLevel2.setAdapter(adapter2);
                });
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
    
    private void loadPhrase() {
        executor.execute(() -> {
            try {
                Phrase phrase = AppDatabase.getInstance(this).phraseDao().getPhraseById(phraseId);
                if (phrase != null) {
                    runOnUiThread(() -> {
                        etTitle.setText(phrase.getTitle());
                        etContent.setText(phrase.getContent());
                        setTypeSelection(phrase.getType());
                    });
                }
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
    
    private void savePhrase() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        
        if (title.isEmpty()) {
            Toast.makeText(this, "请输入话术标题", Toast.LENGTH_SHORT).show();
            etTitle.requestFocus();
            return;
        }
        
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入话术内容", Toast.LENGTH_SHORT).show();
            etContent.requestFocus();
            return;
        }
        
        if (selectedLevel1 == null) {
            Toast.makeText(this, "请选择一级分类", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedLevel2 == null) {
            Toast.makeText(this, "请选择二级分类", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String type = getSelectedType();
        
        executor.execute(() -> {
            try {
                Phrase phrase = new Phrase();
                phrase.setTitle(title);
                phrase.setContent(content);
                phrase.setType(type);
                phrase.setCategoryId(selectedLevel2.getId());
                phrase.setParentCategoryId(selectedLevel1.getId());
                
                if (phraseId != -1) {
                    phrase.setId(phraseId);
                    AppDatabase.getInstance(this).phraseDao().update(phrase);
                } else {
                    AppDatabase.getInstance(this).phraseDao().insert(phrase);
                }
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "保存失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}