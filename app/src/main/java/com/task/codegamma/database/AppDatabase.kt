package com.task.codegamma.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.task.codegamma.util.MyLocationConverter


@Database(entities = [RecentPlace::class], version = 1)
@TypeConverters(MyLocationConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recentPlaceDao(): RecentPlaceDao

}