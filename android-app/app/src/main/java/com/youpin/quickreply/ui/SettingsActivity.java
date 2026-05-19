package com.youpin.quickreply.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.youpin.quickreply.R;
import com.youpin.quickreply.service.FloatingWindowService;

public class SettingsActivity extends AppCompatActivity {
    
    private Switch switchFloatWindow;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        switchFloatWindow = findViewById(R.id.switch_float_window);
        
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        findViewById(R.id.btn_clear_cache).setOnClickListener(v -> {
            Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show();
        });
        
        switchFloatWindow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startService(new Intent(this, FloatingWindowService.class));
                Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT).show();
            } else {
                stopService(new Intent(this, FloatingWindowService.class));
                Toast.makeText(this, "悬浮窗已关闭", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
