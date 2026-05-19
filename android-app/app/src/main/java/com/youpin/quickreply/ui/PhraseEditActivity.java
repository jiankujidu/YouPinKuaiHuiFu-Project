package com.youpin.quickreply.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.youpin.quickreply.R;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Phrase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhraseEditActivity extends AppCompatActivity {
    
    private EditText etTitle, etContent;
    private RadioGroup rgType;
    private Button btnSave;
    
    private ExecutorService executor;
    private long phraseId = -1;
    private String defaultType = "private";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase_edit);
        
        executor = Executors.newSingleThreadExecutor();
        
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        rgType = findViewById(R.id.rg_type);
        btnSave = findViewById(R.id.btn_save);
        
        // 获取传入参数
        phraseId = getIntent().getLongExtra("phrase_id", -1);
        defaultType = getIntent().getStringExtra("type");
        if (defaultType == null) defaultType = "private";
        
        // 设置类型
        switch (defaultType) {
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
        
        // 如果是编辑模式，加载数据
        if (phraseId != -1) {
            setTitle("编辑话术");
            loadPhrase();
        } else {
            setTitle("添加话术");
        }
        
        btnSave.setOnClickListener(v -> savePhrase());
        
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
    
    private void loadPhrase() {
        executor.execute(() -> {
            Phrase phrase = AppDatabase.getInstance(this).phraseDao().getPhraseById(phraseId);
            if (phrase != null) {
                runOnUiThread(() -> {
                    etTitle.setText(phrase.getTitle());
                    etContent.setText(phrase.getContent());
                    switch (phrase.getType()) {
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
                });
            }
        });
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
        
        String type;
        int checkedId = rgType.getCheckedRadioButtonId();
        if (checkedId == R.id.rb_company) {
            type = "company";
        } else if (checkedId == R.id.rb_team) {
            type = "team";
        } else {
            type = "private";
        }
        
        executor.execute(() -> {
            Phrase phrase = new Phrase();
            phrase.setTitle(title);
            phrase.setContent(content);
            phrase.setType(type);
            
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
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
