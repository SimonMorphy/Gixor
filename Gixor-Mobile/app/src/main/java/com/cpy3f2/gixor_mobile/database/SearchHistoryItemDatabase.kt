package com.cpy3f2.gixor_mobile.database

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cpy3f2.gixor_mobile.MyApplication
import com.cpy3f2.gixor_mobile.dao.SearchHistoryItemDao
import com.cpy3f2.gixor_mobile.model.entity.SearchHistoryItem

@Database(entities = [SearchHistoryItem::class], version = 1, exportSchema = false)
abstract class SearchHistoryItemDatabase :RoomDatabase(){
    companion object{
        var database: SearchHistoryItemDatabase
        val Tag:String =  SearchHistoryItemDatabase::class.java.simpleName
        init {
            database = Room.databaseBuilder(MyApplication.getApplicationContext(),SearchHistoryItemDatabase::class.java,"db_search")
                .addCallback(object :Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i(Tag, "onCreate")
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun getSearchHistoryItemDao() : SearchHistoryItemDao
}