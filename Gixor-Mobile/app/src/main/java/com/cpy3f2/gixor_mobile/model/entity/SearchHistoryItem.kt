package com.cpy3f2.gixor_mobile.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity("search_history_item")
data class SearchHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val time : LocalDateTime
)
