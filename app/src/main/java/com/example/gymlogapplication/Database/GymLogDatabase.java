package com.example.gymlogapplication.Database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.gymlogapplication.Database.entities.GymLog;

@Database(entities = GymLog.class, version = 1, exportSchema = false)
public abstract class GymLogDatabase extends RoomDatabase {


}
