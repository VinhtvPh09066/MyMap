package com.example.mymap;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {locationClass.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract locationDAO localDAO();

}
