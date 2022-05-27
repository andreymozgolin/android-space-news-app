package ru.andreymozgolin.spacenews.data

import ru.andreymozgolin.spacenews.api.SpaceNewsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    val spaceNewsService: SpaceNewsService,
    val articleDao: ArticleDao
) {

    fun getArticles(): List<Article> {
        var articles = articleDao.getAll()
        if (articles.isEmpty()) {
            articles = loadArticles()
        }

        return articles
    }

    private fun loadArticles(): List<Article> {
        val articles = spaceNewsService.getArticles().execute().body() ?: listOf()
        articleDao.insertAll(*articles.toTypedArray())
        return articles
    }

    fun getArticle(articleId: Int): Article {
        return articleDao.getArticle(articleId)
    }

}