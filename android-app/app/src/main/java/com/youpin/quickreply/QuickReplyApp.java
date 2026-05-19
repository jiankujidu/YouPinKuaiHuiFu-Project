package com.youpin.quickreply;

import android.app.Application;
import android.util.Log;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Phrase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuickReplyApp extends Application {
    
    private static final String TAG = "QuickReplyApp";
    private ExecutorService executor;
    
    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newSingleThreadExecutor();
        initDefaultData();
    }
    
    private void initDefaultData() {
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(this);
                int count = db.phraseDao().getTotalCount();
                
                if (count == 0) {
                    // 添加默认话术数据
                    addDefaultPhrase("1.欢迎语", "亲，欢迎光临小店，请问有什么可以帮您？", "company", "常用");
                    addDefaultPhrase("2.发货通知", "亲，您的订单已发货，请注意查收！", "company", "订单");
                    addDefaultPhrase("3.感谢购买", "感谢您的购买，期待下次光临！", "company", "常用");
                    addDefaultPhrase("4.请稍等", "请稍等，正在为您查询...", "team", "客服");
                    addDefaultPhrase("5.道歉", "抱歉让您久等了", "private", "常用");
                    addDefaultPhrase("6.结束语", "祝您生活愉快！", "private", "常用");
                    
                    Log.d(TAG, "Default data initialized");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error initializing default data", e);
            }
        });
    }
    
    private void addDefaultPhrase(String title, String content, String type, String category) {
        try {
            Phrase phrase = new Phrase();
            phrase.setTitle(title);
            phrase.setContent(content);
            phrase.setType(type);
            phrase.setCategory(category);
            phrase.setUsageCount(0);
            AppDatabase.getInstance(this).phraseDao().insert(phrase);
        } catch (Exception e) {
            Log.e(TAG, "Error adding default phrase", e);
        }
    }
}
