package com.youpin.quickreply.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.youpin.quickreply.R;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Category;
import com.youpin.quickreply.model.Phrase;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhraseEditActivity extends AppCompatActivity {
    
    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_FILE_PICK = 1002;
    
    private EditText etTitle, etContent;
    private RadioGroup rgType;
    private Button btnSave, btnSelectCategory;
    private TextView tvCategory;
    private LinearLayout layoutAttachments;
    private ImageView ivAttachmentImage;
    private TextView tvAttachmentFile;
    
    private ExecutorService executor;
    private long phraseId = -1;
    private String defaultType = "private";
    private Long selectedCategoryId = null;
    private Long selectedParentCategoryId = null;
    
    private String imagePath = null;
    private String filePath = null;
    private String fileName = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_edit);
        
        executor = Executors.newSingleThreadExecutor();
        
        initViews();
        
        // 获取传入参数
        phraseId = getIntent().getLongExtra("phrase_id", -1);
        defaultType = getIntent().getStringExtra("type");
        if (defaultType == null) defaultType = "private";
        
        // 设置类型
        setTypeSelection(defaultType);
        
        // 如果是编辑模式，加载数据
        if (phraseId != -1) {
            setTitle("编辑话术");
            loadPhrase();
        } else {
            setTitle("添加话术");
        }
        
        btnSave.setOnClickListener(v -> savePhrase());
        btnSelectCategory.setOnClickListener(v -> showCategorySelector());
        
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_image).setOnClickListener(v -> pickImage());
        findViewById(R.id.btn_add_file).setOnClickListener(v -> pickFile());
    }
    
    private void initViews() {
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        rgType = findViewById(R.id.rg_type);
        btnSave = findViewById(R.id.btn_save);
        btnSelectCategory = findViewById(R.id.btn_select_category);
        tvCategory = findViewById(R.id.tv_category);
        layoutAttachments = findViewById(R.id.layout_attachments);
        ivAttachmentImage = findViewById(R.id.iv_attachment_image);
        tvAttachmentFile = findViewById(R.id.tv_attachment_file);
    }
    
    private void setTypeSelection(String type) {
        switch (type) {
            case "company":
                rgType.check(R.id.rb_company);
                break;
            case "team":
                rgType.check(R.id.rb_team);
                break;
            default:
                rgType.check(R.id.rb_private);
                break;
        }
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
                        
                        selectedCategoryId = phrase.getCategoryId();
                        selectedParentCategoryId = phrase.getParentCategoryId();
                        
                        // 加载分类名称
                        if (selectedCategoryId != null) {
                            loadCategoryName(selectedCategoryId);
                        }
                        
                        // 加载附件
                        if (phrase.getImagePath() != null && !phrase.getImagePath().isEmpty()) {
                            imagePath = phrase.getImagePath();
                            showImageAttachment();
                        }
                        if (phrase.getFilePath() != null && !phrase.getFilePath().isEmpty()) {
                            filePath = phrase.getFilePath();
                            fileName = phrase.getFileName();
                            showFileAttachment();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void loadCategoryName(long categoryId) {
        executor.execute(() -> {
            try {
                Category category = AppDatabase.getInstance(this).categoryDao().getCategoryById(categoryId);
                if (category != null) {
                    runOnUiThread(() -> {
                        tvCategory.setText(category.getName());
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void showCategorySelector() {
        executor.execute(() -> {
            try {
                String type = getSelectedType();
                List<Category> level1Cats = AppDatabase.getInstance(this).categoryDao().getLevel1Categories(type);
                
                List<String> items = new ArrayList<>();
                final List<Long> ids = new ArrayList<>();
                final List<Long> parentIds = new ArrayList<>();
                
                for (Category cat1 : level1Cats) {
                    items.add("📁 " + cat1.getName());
                    ids.add(null);
                    parentIds.add(cat1.getId());
                    
                    List<Category> level2Cats = AppDatabase.getInstance(this).categoryDao()
                        .getLevel2Categories(cat1.getId());
                    for (Category cat2 : level2Cats) {
                        items.add("   └ " + cat2.getName());
                        ids.add(cat2.getId());
                        parentIds.add(cat1.getId());
                    }
                }
                
                runOnUiThread(() -> {
                    new AlertDialog.Builder(this)
                        .setTitle("选择分类")
                        .setItems(items.toArray(new String[0]), (dialog, which) -> {
                            selectedCategoryId = ids.get(which);
                            selectedParentCategoryId = parentIds.get(which);
                            tvCategory.setText(items.get(which).replace("📁 ", "").replace("   └ ", ""));
                        })
                        .show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private String getSelectedType() {
        int checkedId = rgType.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_company) return "company";
        if (checkedId == R.id.rb_team) return "team";
        return "private";
    }
    
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    
    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_FILE_PICK);
    }
    
    private void showImageAttachment() {
        layoutAttachments.setVisibility(android.view.View.VISIBLE);
        ivAttachmentImage.setVisibility(android.view.View.VISIBLE);
        // 这里可以加载图片预览
    }
    
    private void showFileAttachment() {
        layoutAttachments.setVisibility(android.view.View.VISIBLE);
        tvAttachmentFile.setVisibility(android.view.View.VISIBLE);
        tvAttachmentFile.setText(fileName);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (requestCode == REQUEST_IMAGE_PICK) {
                    imagePath = uri.toString();
                    showImageAttachment();
                } else if (requestCode == REQUEST_FILE_PICK) {
                    filePath = uri.toString();
                    fileName = uri.getLastPathSegment();
                    showFileAttachment();
                }
            }
        }
    }
    
    private void savePhrase() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        
        if (title.isEmpty()) {
            Toast.makeText(this, "请输入话术标题", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入话术内容", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String type = getSelectedType();
        
        executor.execute(() -> {
            try {
                Phrase phrase = new Phrase();
                phrase.setTitle(title);
                phrase.setContent(content);
                phrase.setType(type);
                phrase.setCategoryId(selectedCategoryId);
                phrase.setParentCategoryId(selectedParentCategoryId);
                phrase.setImagePath(imagePath);
                phrase.setFilePath(filePath);
                phrase.setFileName(fileName);
                
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
