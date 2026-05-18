package com.quickreply.wechat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.quickreply.wechat.R;
import com.quickreply.wechat.service.FloatingWindowService;

public class SettingsActivity extends AppCompatActivity {
    
    private Switch switchFloating;
    private LinearLayout layoutPermission;
    private LinearLayout layoutCategory;
    private LinearLayout layoutAbout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initToolbar();
        initViews();
    }
    
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("设置");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void initViews() {
        switchFloating = findViewById(R.id.switch_floating);
        layoutPermission = findViewById(R.id.layout_permission);
        layoutCategory = findViewById(R.id.layout_category);
        layoutAbout = findViewById(R.id.layout_about);
        
        // 悬浮窗开关
        switchFloating.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startService(new Intent(this, FloatingWindowService.class));
                Toast.makeText(this, "悬浮窗已开启", Toast.LENGTH_SHORT).show();
            } else {
                stopService(new Intent(this, FloatingWindowService.class));
                Toast.makeText(this, "悬浮窗已关闭", Toast.LENGTH_SHORT).show();
            }
        });
        
        // 权限设置
        layoutPermission.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });
        
        // 分类管理
        layoutCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoryManageActivity.class);
            startActivity(intent);
        });
        
        // 关于
        layoutAbout.setOnClickListener(v -> {
            Toast.makeText(this, "快捷回复 v1.0.0\n微信风格快捷回复工具", Toast.LENGTH_SHORT).show();
        });
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
