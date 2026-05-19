package com.youpin.quickreply.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.youpin.quickreply.model.Phrase;

import java.util.List;

@Dao
public interface PhraseDao {
    
    @Query("SELECT * FROM phrases WHERE type = :type ORDER BY updateTime DESC")
    LiveData<List<Phrase>> getPhrasesByType(String type);
    
    @Query("SELECT * FROM phrases WHERE type = :type ORDER BY updateTime DESC")
    List<Phrase> getPhrasesByTypeSync(String type);
    
    @Query("SELECT * FROM phrases WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY updateTime DESC")
    LiveData<List<Phrase>> searchPhrases(String keyword);
    
    @Query("SELECT * FROM phrases WHERE type = :type AND (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%') ORDER BY updateTime DESC")
    LiveData<List<Phrase>> searchPhrasesByType(String type, String keyword);
    
    @Query("SELECT * FROM phrases WHERE id = :id")
    Phrase getPhraseById(long id);
    
    @Insert
    long insert(Phrase phrase);
    
    @Update
    void update(Phrase phrase);
    
    @Delete
    void delete(Phrase phrase);
    
    @Query("DELETE FROM phrases WHERE id = :id")
    void deleteById(long id);
    
    @Query("SELECT COUNT(*) FROM phrases")
    int getTotalCount();
    
    @Query("SELECT COALESCE(SUM(usageCount), 0) FROM phrases")
    int getTotalUseCount();
    
    @Query("UPDATE phrases SET usageCount = usageCount + 1 WHERE id = :id")
    void incrementUseCount(long id);
    
    @Query("SELECT DISTINCT category FROM phrases WHERE category IS NOT NULL AND category != ''")
    List<String> getAllCategories();
    
    @Query("SELECT * FROM phrases WHERE title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' ORDER BY updateTime DESC")
    List<Phrase> searchPhrasesSync(String keyword);
}
