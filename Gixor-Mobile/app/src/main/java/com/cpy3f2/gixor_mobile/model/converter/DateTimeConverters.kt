package com.cpy3f2.gixor_mobile.model.converter

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class DateTimeConverters {
    @SuppressLint("NewApi")
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @SuppressLint("NewApi")
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            try {
                LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
            } catch (e: Exception) {
                null
            }
        }
    }

    @SuppressLint("NewApi")
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    companion object {
        @SuppressLint("NewApi")
        fun formatRelativeTime(dateTimeStr: String?): String {
            if (dateTimeStr == null) return "未知时间"
            
            try {
                val dateTime = LocalDateTime.parse(
                    dateTimeStr, 
                    DateTimeFormatter.ISO_DATE_TIME
                )
                
                val now = LocalDateTime.now()
                val minutes = ChronoUnit.MINUTES.between(dateTime, now)
                val hours = ChronoUnit.HOURS.between(dateTime, now)
                val days = ChronoUnit.DAYS.between(dateTime, now)
                val months = ChronoUnit.MONTHS.between(dateTime, now)
                val years = ChronoUnit.YEARS.between(dateTime, now)

                return when {
                    abs(minutes) < 1 -> "刚刚"
                    abs(minutes) < 60 -> "${abs(minutes)}分钟前"
                    abs(hours) < 24 -> "${abs(hours)}小时前"
                    abs(days) < 30 -> "${abs(days)}天前"
                    abs(months) < 12 -> "${abs(months)}个月前"
                    else -> "${abs(years)}年前"
                }
            } catch (e: Exception) {
                return "时间格式错误"
            }
        }
    }
} 