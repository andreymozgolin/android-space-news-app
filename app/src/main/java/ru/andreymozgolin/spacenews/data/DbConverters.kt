package ru.andreymozgolin.spacenews.data

import android.util.Log
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DbConverters {

    @TypeConverter
    fun fromPublishedAt(value: String?): Date? {
        return value?.let { format.parse(it) }
    }

    @TypeConverter
    fun dateToPublishedAt(date: Date?): String? {
        return format.format(date)
    }

    companion object {
        private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    }
}