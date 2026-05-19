package com.youpin.quickreply.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "phrases")
public class Phrase {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String title;
    private String content;
    private String type; // company, team, private
    private String category;
    private int usageCount;
    private long createTime;
    private long updateTime;
    
    public Phrase() {
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
        this.usageCount = 0;
    }
    
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getUsageCount() { return usageCount; }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
}
