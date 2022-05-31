package ru.andreymozgolin.spacenews.data

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import ru.andreymozgolin.spacenews.api.SpaceNewsService
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

private const val TAG = "ArticleRepository"

@Singleton
class ArticleRepository @Inject constructor(
    val spaceNewsService: SpaceNewsService,
    val articleDao: ArticleDao
) {

    fun getArticles(reload: Boolean = false): Observable<List<Article>> = Observable.create {
        var articles = listOf<Article>()
        if (reload) {
            articleDao.dropAll()
            Log.d(TAG, "Drop articles from local storage.")
        } else {
            articles = articleDao.getAll()
            Log.d(TAG, "Loaded ${articles.size} articles from local storage.")
        }

        if (articles.isEmpty()) {
            articles = loadArticles()
            Log.d(TAG, "Loaded ${articles.size} articles from remote storage.")
        }
        it.onNext(articles)
        it.onComplete()
    }

    fun getMoreArticles(): Observable<List<Article>> = Observable.create {
        var articles = articleDao.getAll()
        val minId = articles.map { article ->  article.id }.reduce(::min)
        Log.d(TAG, "Found last loaded article with id = $minId")

        val newArticles = loadArticles(minId)
        Log.d(TAG, "Loaded ${newArticles.size} articles from remote storage.")
        it.onNext(articles + newArticles)
        it.onComplete()
    }

    private fun loadArticles(fromId: Int? = null): List<Article> {
        val articles = spaceNewsService.getArticles(fromId = fromId).execute().body() ?: listOf()
        articleDao.insertAll(*articles.toTypedArray())
        return articles
    }

    fun getArticle(articleId: Int): Article {
        Log.d(TAG, "Load article with id = $articleId from local storage.")
        return articleDao.getArticle(articleId)
    }

}