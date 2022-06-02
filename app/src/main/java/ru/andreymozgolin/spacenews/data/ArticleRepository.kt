package ru.andreymozgolin.spacenews.data

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import ru.andreymozgolin.spacenews.api.SpaceNewsService
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ArticleRepository"

@Singleton
class ArticleRepository @Inject constructor(
    val spaceNewsService: SpaceNewsService,
    val articleDao: ArticleDao
) {

    fun getArticles(reload: Boolean = false): Observable<List<Article>> = Observable.create {
        var articles = if (reload) {
            Log.d(TAG, "Drop articles from local storage.")
            articleDao.dropAll()
            listOf<Article>()
        } else {
            articleDao.getAll().also { result ->
                Log.d(TAG, "Loaded ${result.size} articles from local storage.")
            }
        }

        if (articles.isEmpty()) {
            articles = loadArticles(spaceNewsService.getArticles())
        }
        it.onNext(articles)
        it.onComplete()
    }

    fun getMoreArticles(): Observable<List<Article>> = Observable.create {
        articleDao.getMinId()?.also { minId ->
            it.onNext(loadArticles(spaceNewsService.getArticles(minId)))
        } ?: it.onNext(listOf())
        it.onComplete()
    }

    fun getLastArticles(): Observable<List<Article>> = Observable.create {
        articleDao.getMaxId()?.also { maxId ->
            it.onNext(loadArticles(spaceNewsService.getLastArticles(maxId)))
        } ?: it.onNext(listOf())
        it.onComplete()
    }

    private fun loadArticles(remoteCall: Call<List<Article>>): List<Article> {
        val articles = remoteCall.execute().body() ?: emptyList()
        Log.d(TAG, "Loaded ${articles.size} articles from remote storage.")

        if (articles.isNotEmpty()) {
            Log.d(TAG, "Save articles to local storage.")
            articleDao.insertAll(articles)
        }

        return articles
    }

    fun getArticle(articleId: Int): Article {
        Log.d(TAG, "Load article with id = $articleId from local storage.")
        return articleDao.getArticle(articleId)
    }

}