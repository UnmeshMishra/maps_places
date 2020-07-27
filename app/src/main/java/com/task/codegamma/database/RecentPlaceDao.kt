package com.task.codegamma.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecentPlaceDao {

    @Query("SELECT * FROM recent_places")
    fun getAll(): LiveData<List<RecentPlace>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: RecentPlace)

    @Insert
    suspend fun insertAll(places: List<RecentPlace>)

    @Delete
    suspend fun delete(place: RecentPlace)

}