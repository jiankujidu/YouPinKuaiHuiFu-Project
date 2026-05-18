package com.quickreply.wechat.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "categories")
public class Category implements Serializable {
    
    public static final String TYPE_COMPANY = "company";
    public static final String TYPE_TEAM = "team";
    public static final String TYPE_PRIVATE = "private";
    
    @PrimaryKey
    @NonNull
    private String id;
    
    private String name;
    private String type; // company, team, private
    private int color;
    private int sortOrder;
    private long createTime;
    private int phraseCount;
    
    public Category() {
        this.id = UUID.randomUUID().toString();
        this.createTime = System.currentTimeMillis();
        this.phraseCount = 0;
        this.sortOrder = 0;
    }
    
    @NonNull
    public String getId() {
        return id;
    }
    
    public void setId(@NonNull String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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
    
    public int getPhraseCount() {
        return phraseCount;
    }
    
    public void setPhraseCount(int phraseCount) {
        this.phraseCount = phraseCount;
    }
    
    public String getTypeDisplayName() {
        switch (type) {
            case TYPE_COMPANY:
                return "公司话术";
            case TYPE_TEAM:
                return "小组话术";
            case TYPE_PRIVATE:
                return "私人话术";
            default:
                return "未分类";
        }
    }
}
