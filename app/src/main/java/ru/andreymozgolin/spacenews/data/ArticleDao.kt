package ru.andreymozgolin.spacenews.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getAll(): List<Article>

    @Query("SELECT * FROM article WHERE id = :articleId")
    fun getArticle(articleId: Int): Article

    @Query("SELECT min(id) as id FROM article")
    fun getMinId(): Int

    @Insert
    fun insertAll(vararg article: Article)

    @Query("DELETE FROM article")
    fun dropAll()
}