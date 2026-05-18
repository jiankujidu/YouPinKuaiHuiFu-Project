package com.quickreply.wechat;

import android.app.Application;
import android.content.Context;

import com.quickreply.wechat.database.AppDatabase;

public class QuickReplyApp extends Application {
    
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // Initialize database
        AppDatabase.getInstance(context);
    }
    
    public static Context getContext() {
        return context;
    }
}
