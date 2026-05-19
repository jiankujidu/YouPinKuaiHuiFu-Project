package com.youpin.quickreply.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "categories",
        foreignKeys = @ForeignKey(
            entity = Category.class,
            parentColumns = "id",
            childColumns = "parentId",
            onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("parentId")})
public class Category {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;           // 分类名称
    private String type;           // company, team, private
    private Long parentId;         // 父分类ID，null表示一级分类
    private int sortOrder;         // 排序
    private String color;          // 色条颜色
    private long createTime;
    private long updateTime;
    
    // 非数据库字段，用于显示
    private int phraseCount;       // 话术数量
    private boolean expanded;      // 是否展开
    private int level;             // 层级 0=一级 1=二级
    
    public Category() {
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
        this.expanded = false;
    }
    
    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    
    public int getPhraseCount() { return phraseCount; }
    public void setPhraseCount(int phraseCount) { this.phraseCount = phraseCount; }
    
    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}
