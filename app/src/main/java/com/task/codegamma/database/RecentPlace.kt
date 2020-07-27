package com.task.codegamma.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.codegamma.model.MyLocation

@Entity(tableName = "recent_places")
data class RecentPlace(
    @PrimaryKey @NonNull val id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "address") val address: String?,

    @ColumnInfo(name = "latlng") val latLng: MyLocation?
)