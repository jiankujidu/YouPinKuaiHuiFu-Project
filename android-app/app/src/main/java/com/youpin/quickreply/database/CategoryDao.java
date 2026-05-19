package com.youpin.quickreply.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.youpin.quickreply.model.Category;
import java.util.List;

@Dao
public interface CategoryDao {
    
    @Query("SELECT * FROM categories WHERE type = :type AND parentId IS NULL ORDER BY sortOrder ASC")
    List<Category> getLevel1Categories(String type);
    
    @Query("SELECT * FROM categories WHERE parentId = :parentId ORDER BY sortOrder ASC")
    List<Category> getLevel2Categories(long parentId);
    
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY sortOrder ASC")
    List<Category> getAllCategories(String type);
    
    @Query("SELECT COUNT(*) FROM phrases WHERE categoryId = :categoryId")
    int getPhraseCountByCategory(long categoryId);
    
    @Query("SELECT COUNT(*) FROM phrases WHERE parentCategoryId = :parentId")
    int getPhraseCountByParentCategory(long parentId);
    
    @Insert
    long insert(Category category);
    
    @Update
    void update(Category category);
    
    @Delete
    void delete(Category category);
    
    @Query("DELETE FROM categories WHERE id = :id")
    void deleteById(long id);
    
    @Query("SELECT * FROM categories WHERE id = :id")
    Category getCategoryById(long id);
}
