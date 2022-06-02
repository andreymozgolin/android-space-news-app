package ru.andreymozgolin.spacenews.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article ORDER BY publishedAt DESC")
    fun getAll(): List<Article>

    @Query("SELECT * FROM article WHERE id = :articleId")
    fun getArticle(articleId: Int): Article

    @Query("SELECT max(id) FROM article")
    fun getMaxId(): Int?

    @Query("SELECT min(id) FROM article")
    fun getMinId(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(articles: List<Article>)

    @Query("DELETE FROM article")
    fun dropAll()
}