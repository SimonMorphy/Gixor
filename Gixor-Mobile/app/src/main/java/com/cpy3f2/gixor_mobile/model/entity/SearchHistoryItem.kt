package com.cpy3f2.gixor_mobile.model.entity

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "search_history_item")
data class SearchHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @SuppressLint("NewApi") val time: LocalDateTime = LocalDateTime.now()
)
