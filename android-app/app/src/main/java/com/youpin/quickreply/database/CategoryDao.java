package com.youpin.quickreply.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.youpin.quickreply.model.Category;
import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY sortOrder ASC")
    LiveData<List<Category>> getCategoriesByType(String type);
    
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY sortOrder ASC")
    List<Category> getCategoriesByTypeSync(String type);
    
    @Insert
    long insert(Category category);
    
    @Update
    void update(Category category);
    
    @Delete
    void delete(Category category);
    
    @Query("DELETE FROM categories WHERE id = :id")
    void deleteById(long id);
}
