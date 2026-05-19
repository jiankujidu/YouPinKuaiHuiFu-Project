package com.youpin.quickreply.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.youpin.quickreply.model.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    LiveData<User> getUser();
    
    @Query("SELECT * FROM users LIMIT 1")
    User getUserSync();
    
    @Insert
    long insert(User user);
    
    @Update
    void update(User user);
    
    @Query("DELETE FROM users")
    void deleteAll();
}
