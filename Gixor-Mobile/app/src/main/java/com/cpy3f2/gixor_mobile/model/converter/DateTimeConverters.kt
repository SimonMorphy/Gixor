package com.cpy3f2.gixor_mobile.model.converter

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverters {
    @SuppressLint("NewApi")
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @SuppressLint("NewApi")
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }

    @SuppressLint("NewApi")
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
} 