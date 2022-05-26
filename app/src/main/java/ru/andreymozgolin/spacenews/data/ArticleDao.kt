package ru.andreymozgolin.spacenews.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getAll(): List<Article>

    @Insert
    fun insertAll(vararg article: Article)
}