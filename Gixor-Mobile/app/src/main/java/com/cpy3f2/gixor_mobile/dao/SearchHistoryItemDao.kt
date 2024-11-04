package com.cpy3f2.gixor_mobile.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cpy3f2.gixor_mobile.model.entity.SearchHistoryItem
import java.time.LocalDateTime

@Dao
interface SearchHistoryItemDao {
    @Query("SELECT * FROM search_history_item ORDER BY time DESC")
    fun getAll(): List<SearchHistoryItem>

    @Insert
    suspend fun insert(item: SearchHistoryItem)

    @Insert
    suspend fun insertAll(items: List<SearchHistoryItem>)

    @Delete
    suspend fun delete(item: SearchHistoryItem)

    @Delete
    suspend fun deleteAll(items: List<SearchHistoryItem>)

    @Query("DELETE FROM search_history_item")
    suspend fun clearAll()

    @Query("SELECT * FROM search_history_item WHERE name = :searchText LIMIT 1")
    suspend fun findByName(searchText: String): SearchHistoryItem?

    @Query("UPDATE search_history_item SET time = :newTime WHERE name = :searchText")
    suspend fun updateTime(searchText: String, newTime: LocalDateTime)
}