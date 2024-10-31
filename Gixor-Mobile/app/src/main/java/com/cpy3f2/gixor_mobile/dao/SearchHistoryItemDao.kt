package com.cpy3f2.gixor_mobile.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cpy3f2.gixor_mobile.model.entity.SearchHistoryItem
import retrofit2.http.DELETE

@Dao
interface SearchHistoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg searchHistoryItem: SearchHistoryItem)

    @Query("SELECT * FROM search_history_item")
    suspend fun getAll() : List<SearchHistoryItem>

    @Query("SELECT * FROM search_history_item WHERE id=:id")
    suspend fun findByName(id: Int) : SearchHistoryItem

    @DELETE
    suspend fun delete(searchHistoryItem: SearchHistoryItem)

    @DELETE(("DELETE FROM search_history_item"))
    suspend fun deleteAll(vararg searchHistoryItem: SearchHistoryItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg searchHistoryItem: SearchHistoryItem)

}