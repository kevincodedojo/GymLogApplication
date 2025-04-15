package com.example.gymlogapplication.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.gymlogapplication.database.entities.GymLog;

import java.util.List;

@Dao
public interface GymLogDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GymLog gymLog);

    @Query("Select * from " + GymLogDatabase.GYM_LOG_TABLE + " ORDER BY localDate DESC")
    List<GymLog> getAllRecords();

    @Query("Select * from " + GymLogDatabase.GYM_LOG_TABLE + " where userId = :loggedInUserId ORDER BY localDate DESC")
    List<GymLog> getRecordsetUserId(int loggedInUserId);

}
