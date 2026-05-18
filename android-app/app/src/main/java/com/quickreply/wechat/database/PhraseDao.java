package com.quickreply.wechat.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.quickreply.wechat.model.Phrase;

import java.util.List;

@Dao
public interface PhraseDao {
    
    @Query("SELECT * FROM phrases ORDER BY sortOrder ASC, updateTime DESC")
    LiveData<List<Phrase>> getAllPhrases();
    
    @Query("SELECT * FROM phrases WHERE categoryId = :categoryId ORDER BY sortOrder ASC, updateTime DESC")
    LiveData<List<Phrase>> getPhrasesByCategory(String categoryId);
    
    @Query("SELECT * FROM phrases WHERE content LIKE '%' || :keyword || '%' ORDER BY useCount DESC, updateTime DESC")
    LiveData<List<Phrase>> searchPhrases(String keyword);
    
    @Query("SELECT * FROM phrases WHERE categoryType = :type ORDER BY sortOrder ASC")
    LiveData<List<Phrase>> getPhrasesByType(String type);
    
    @Insert
    void insert(Phrase phrase);
    
    @Update
    void update(Phrase phrase);
    
    @Delete
    void delete(Phrase phrase);
    
    @Query("DELETE FROM phrases WHERE id = :phraseId")
    void deleteById(String phraseId);
    
    @Query("SELECT * FROM phrases WHERE id = :phraseId")
    Phrase getPhraseById(String phraseId);
    
    @Query("UPDATE phrases SET useCount = useCount + 1, updateTime = :time WHERE id = :phraseId")
    void incrementUseCount(String phraseId, long time);
    
    @Query("SELECT COUNT(*) FROM phrases")
    int getPhraseCount();
    
    @Query("SELECT COUNT(*) FROM phrases WHERE categoryId = :categoryId")
    int getPhraseCountByCategory(String categoryId);
}
