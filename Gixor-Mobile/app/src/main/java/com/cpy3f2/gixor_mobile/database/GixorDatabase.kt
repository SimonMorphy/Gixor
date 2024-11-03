package com.cpy3f2.gixor_mobile.database

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.TypeConverters
import com.cpy3f2.gixor_mobile.MyApplication
import com.cpy3f2.gixor_mobile.dao.SearchHistoryItemDao
import com.cpy3f2.gixor_mobile.model.entity.SearchHistoryItem
import com.cpy3f2.gixor_mobile.model.converter.DateTimeConverters

@Database(
    entities = [
        SearchHistoryItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class GixorDatabase : RoomDatabase() {
    companion object {
        var database: GixorDatabase
        val Tag: String = GixorDatabase::class.java.simpleName
        init {
            database = Room.databaseBuilder(MyApplication.getApplicationContext(), GixorDatabase::class.java, "db_gixor")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.i(Tag, "onCreate")
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun getSearchHistoryItemDao(): SearchHistoryItemDao
}