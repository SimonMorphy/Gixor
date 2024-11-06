package com.cpy3f2.gixor_mobile.model.converter

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
        private val API_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        @RequiresApi(Build.VERSION_CODES.O)
        private val ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        @SuppressLint("NewApi")
        fun formatDateTime(dateTime: LocalDateTime?): String {
            if (dateTime == null) return "未知时间"
            
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
        }

        @SuppressLint("NewApi")
        fun formatRelativeTime(dateTime: LocalDateTime?): String {
            return formatDateTime(dateTime)
        }

        @SuppressLint("NewApi")
        fun formatDateTimeString(dateTimeStr: String?): String {
            return try {
                dateTimeStr?.let {
                    val dateTime = LocalDateTime.parse(it, API_FORMATTER)
                    formatDateTime(dateTime)
                } ?: "未知时间"
            } catch (e: Exception) {
                try {
                    // 尝试使用 ISO 格式解析
                    dateTimeStr?.let {
                        val dateTime = LocalDateTime.parse(it, ISO_FORMATTER)
                        formatDateTime(dateTime)
                    } ?: "未知时间"
                } catch (e: Exception) {
                    "未知时间"
                }
            }
        }

        // 新增：处理 String 转 LocalDateTime
        @SuppressLint("NewApi")
        fun parseDateTime(dateTimeStr: String?): LocalDateTime? {
            return try {
                dateTimeStr?.let {
                    LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                }
            } catch (e: Exception) {
                null
            }
        }

        // 新增：格式化相对时间（从字符串输入）
        @SuppressLint("NewApi")
        fun formatRelativeTimeFromString(dateTimeStr: String?): String {
            return formatDateTime(parseDateTime(dateTimeStr))
        }

        // 新增：格式化完整日期时间
        @SuppressLint("NewApi")
        fun formatFullDateTime(dateTime: LocalDateTime?): String {
            return dateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ?: "未知时间"
        }

        // 新增：格式化日期
        @SuppressLint("NewApi")
        fun formatDate(dateTime: LocalDateTime?): String {
            return dateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "未知时间"
        }
    }
} 