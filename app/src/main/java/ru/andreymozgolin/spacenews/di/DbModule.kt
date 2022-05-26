package ru.andreymozgolin.spacenews.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.andreymozgolin.spacenews.data.AppDatabase
import ru.andreymozgolin.spacenews.data.ArticleDao
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database.db").build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(database: AppDatabase): ArticleDao {
        return database.getArticleDao()
    }
}