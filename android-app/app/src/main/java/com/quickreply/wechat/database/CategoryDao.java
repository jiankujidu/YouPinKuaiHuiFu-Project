package com.quickreply.wechat.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.quickreply.wechat.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    LiveData<List<Category>> getAllCategories();
    
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY sortOrder ASC")
    LiveData<List<Category>> getCategoriesByType(String type);
    
    @Insert
    void insert(Category category);
    
    @Update
    void update(Category category);
    
    @Delete
    void delete(Category category);
    
    @Query("DELETE FROM categories WHERE id = :categoryId")
    void deleteById(String categoryId);
    
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    Category getCategoryById(String categoryId);
    
    @Query("UPDATE categories SET phraseCount = (SELECT COUNT(*) FROM phrases WHERE categoryId = :categoryId) WHERE id = :categoryId")
    void updatePhraseCount(String categoryId);
}
