package ru.andreymozgolin.spacenews.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Article::class], version = 1)
@TypeConverters(DbConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}