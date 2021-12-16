package com.daitan.example.socialnetwork.model.common

import androidx.room.TypeConverter
import java.util.*

class DatabaseDateConverter {

    @TypeConverter
    fun longToDate(time: Long): Date {
        return Date(time)
    }

    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }
}