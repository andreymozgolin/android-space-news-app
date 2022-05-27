package ru.andreymozgolin.spacenews.articles

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import ru.andreymozgolin.spacenews.data.Article
import ru.andreymozgolin.spacenews.data.ArticleRepository
import javax.inject.Inject
import javax.inject.Singleton

sealed class ArticlesState {
    object Loading : ArticlesState()
    class Result(val data: List<Article>): ArticlesState()
    class Error(val error: String): ArticlesState()
}

@Singleton
class ArticlesViewModel @Inject constructor(
    val repository: ArticleRepository
) {
    val articles = Observable.create<ArticlesState> {
        it.onNext(ArticlesState.Loading)
        val articles = repository.getArticles()
        it.onNext(ArticlesState.Result(articles))
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun getArticle(articleId: Int): Single<Article> {
        return Single.create<Article> {
            it.onSuccess(repository.getArticle(articleId))
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}