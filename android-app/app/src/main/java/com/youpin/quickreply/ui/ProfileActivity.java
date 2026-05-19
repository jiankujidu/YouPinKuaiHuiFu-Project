package com.youpin.quickreply.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.youpin.quickreply.R;
import com.youpin.quickreply.database.AppDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {
    
    private TextView tvNickname, tvUsername, tvTotalPhrases, tvTotalUsage;
    private Button btnSettings, btnAbout;
    
    private ExecutorService executor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        executor = Executors.newSingleThreadExecutor();
        
        tvNickname = findViewById(R.id.tv_nickname);
        tvUsername = findViewById(R.id.tv_username);
        tvTotalPhrases = findViewById(R.id.tv_total_phrases);
        tvTotalUsage = findViewById(R.id.tv_total_usage);
        btnSettings = findViewById(R.id.btn_settings);
        btnAbout = findViewById(R.id.btn_about);
        
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
        
        btnAbout.setOnClickListener(v -> {
            Toast.makeText(this, "优品快回复 v1.0.0", Toast.LENGTH_SHORT).show();
        });
        
        loadStats();
    }
    
    private void loadStats() {
        executor.execute(() -> {
            int totalPhrases = AppDatabase.getInstance(this).phraseDao().getTotalCount();
            int totalUsage = AppDatabase.getInstance(this).phraseDao().getTotalUseCount();
            
            runOnUiThread(() -> {
                tvTotalPhrases.setText(String.valueOf(totalPhrases));
                tvTotalUsage.setText(String.valueOf(totalUsage));
                tvNickname.setText("用户");
                tvUsername.setText("未登录");
            });
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
