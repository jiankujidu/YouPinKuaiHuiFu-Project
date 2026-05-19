package com.youpin.quickreply;

import android.app.Application;
import android.util.Log;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Category;
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
                
                // 检查是否已有数据
                int categoryCount = db.categoryDao().getAllCategories("company").size();
                if (categoryCount > 0) {
                    Log.d(TAG, "Data already initialized");
                    return;
                }
                
                // 创建一级分类
                long cat1Id = createCategory("欢迎语", "company", null, "#07C160", 0);
                long cat2Id = createCategory("订单处理", "company", null, "#409EFF", 1);
                long cat3Id = createCategory("售后服务", "company", null, "#E6A23C", 2);
                
                // 创建二级分类
                long sub1Id = createCategory("开场白", "company", cat1Id, null, 0);
                long sub2Id = createCategory("结束语", "company", cat1Id, null, 1);
                
                long sub3Id = createCategory("发货通知", "company", cat2Id, null, 0);
                long sub4Id = createCategory("物流查询", "company", cat2Id, null, 1);
                
                long sub5Id = createCategory("退换货", "company", cat3Id, null, 0);
                long sub6Id = createCategory("投诉处理", "company", cat3Id, null, 1);
                
                // 添加话术
                addPhrase("欢迎光临", "亲，欢迎光临小店，请问有什么可以帮您？", "company", sub1Id, cat1Id);
                addPhrase("感谢购买", "感谢您的购买，期待下次光临！", "company", sub2Id, cat1Id);
                addPhrase("发货通知", "亲，您的订单已发货，请注意查收！", "company", sub3Id, cat2Id);
                addPhrase("物流查询", "请稍等，正在为您查询物流信息...", "company", sub4Id, cat2Id);
                addPhrase("退换货", "好的，请提供订单号，我帮您处理退换货", "company", sub5Id, cat3Id);
                addPhrase("投诉处理", "非常抱歉给您带来不好的体验，我们会尽快处理", "company", sub6Id, cat3Id);
                
                // 添加带图片的话术示例
                Phrase imagePhrase = new Phrase();
                imagePhrase.setTitle("产品展示");
                imagePhrase.setContent("这是我们产品的图片，请查看");
                imagePhrase.setType("company");
                imagePhrase.setCategoryId(sub1Id);
                imagePhrase.setParentCategoryId(cat1Id);
                imagePhrase.setImagePath("sample_image"); // 示例路径
                db.phraseDao().insert(imagePhrase);
                
                Log.d(TAG, "Default data initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing default data", e);
            }
        });
    }
    
    private long createCategory(String name, String type, Long parentId, String color, int sortOrder) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setType(type);
            category.setParentId(parentId);
            category.setColor(color);
            category.setSortOrder(sortOrder);
            if (parentId == null) {
                category.setLevel(0);
            } else {
                category.setLevel(1);
            }
            return AppDatabase.getInstance(this).categoryDao().insert(category);
        } catch (Exception e) {
            Log.e(TAG, "Error creating category", e);
            return -1;
        }
    }
    
    private void addPhrase(String title, String content, String type, long categoryId, long parentCategoryId) {
        try {
            Phrase phrase = new Phrase();
            phrase.setTitle(title);
            phrase.setContent(content);
            phrase.setType(type);
            phrase.setCategoryId(categoryId);
            phrase.setParentCategoryId(parentCategoryId);
            AppDatabase.getInstance(this).phraseDao().insert(phrase);
        } catch (Exception e) {
            Log.e(TAG, "Error adding phrase", e);
        }
    }
}
