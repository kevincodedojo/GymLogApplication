package com.example.gymlogapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gymlogapplication.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delate(User user);

    @Query(" SELECT * FROM " + GymLogDatabase.USER_TABLE + " ORDER BY username")
    List<User> getAllUsers();

    @Query(" DELETE from " + GymLogDatabase.USER_TABLE)
    void delateAll();
}
