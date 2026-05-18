package com.quickreply.wechat.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "phrases")
public class Phrase implements Serializable {
    
    @PrimaryKey
    @NonNull
    private String id;
    
    private String content;
    private String categoryId;
    private String categoryName;
    private String categoryType; // company, team, private
    private int color;
    private int sortOrder;
    private long createTime;
    private long updateTime;
    private int useCount;
    
    public Phrase() {
        this.id = UUID.randomUUID().toString();
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
        this.useCount = 0;
        this.sortOrder = 0;
    }
    
    @NonNull
    public String getId() {
        return id;
    }
    
    public void setId(@NonNull String id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getCategoryType() {
        return categoryType;
    }
    
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
    
    public int getColor() {
        return color;
    }
    
    public void setColor(int color) {
        this.color = color;
    }
    
    public int getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    public long getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    
    public int getUseCount() {
        return useCount;
    }
    
    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
    
    public void incrementUseCount() {
        this.useCount++;
    }
}
