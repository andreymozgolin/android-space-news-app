package ru.andreymozgolin.spacenews.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article ORDER BY publishedAt DESC")
    fun getAll(): List<Article>

    @Query("SELECT * FROM article WHERE id = :articleId")
    fun getArticle(articleId: Int): Article

    @Insert
    fun insertAll(vararg article: Article)

    @Query("DELETE FROM article")
    fun dropAll()
}