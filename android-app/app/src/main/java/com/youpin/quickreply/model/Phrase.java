package com.youpin.quickreply.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "phrases",
        foreignKeys = {
            @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.SET_NULL
            )
        },
        indices = {@Index("categoryId"), @Index("type")})
public class Phrase {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String title;           // 话术标题
    private String content;         // 话术内容（文本）
    private String type;            // company, team, private
    private Long categoryId;        // 所属二级分类ID
    private Long parentCategoryId;  // 所属一级分类ID
    private int usageCount;         // 使用次数
    
    // 多媒体内容
    private String imagePath;       // 图片路径
    private String filePath;        // 文件路径
    private String fileName;        // 文件名
    private long fileSize;          // 文件大小
    
    private long createTime;
    private long updateTime;
    
    // 非数据库字段
    private String categoryName;    // 分类名称（用于显示）
    
    public Phrase() {
        this.createTime = System.currentTimeMillis();
        this.updateTime = System.currentTimeMillis();
        this.usageCount = 0;
    }
    
    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public Long getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(Long parentCategoryId) { this.parentCategoryId = parentCategoryId; }
    
    public int getUsageCount() { return usageCount; }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    
    public long getCreateTime() { return createTime; }
    public void setCreateTime(long createTime) { this.createTime = createTime; }
    
    public long getUpdateTime() { return updateTime; }
    public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    // 是否有附件
    public boolean hasAttachment() {
        return (imagePath != null && !imagePath.isEmpty()) || 
               (filePath != null && !filePath.isEmpty());
    }
    
    // 获取附件类型
    public String getAttachmentType() {
        if (imagePath != null && !imagePath.isEmpty()) return "image";
        if (filePath != null && !filePath.isEmpty()) return "file";
        return null;
    }
}
