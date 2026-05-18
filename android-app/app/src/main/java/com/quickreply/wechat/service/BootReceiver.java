package com.quickreply.wechat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // 开机启动悬浮窗服务
            Intent serviceIntent = new Intent(context, FloatingWindowService.class);
            context.startService(serviceIntent);
        }
    }
}
