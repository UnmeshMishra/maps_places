package com.task.codegamma.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.task.codegamma.model.MyLocation

class MyLocationConverter {

    companion object{
        @TypeConverter
        @JvmStatic
        fun fromMyLocation(value: MyLocation): String {
            val gson = Gson()
            return gson.toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toMyLocation(value: String): MyLocation {
            val gson = Gson()
            return gson.fromJson(value,
                MyLocation::class.java) as MyLocation
        }
    }
}