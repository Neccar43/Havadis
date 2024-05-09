package com.novacodestudios.havadis.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.novacodestudios.havadis.data.remote.dto.Article

@Database(entities = [Article::class], version = 2 , exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase:RoomDatabase() {
    abstract fun articleDao():ArticleDao
}