package ru.andreymozgolin.spacenews.articles

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.andreymozgolin.spacenews.data.Article
import ru.andreymozgolin.spacenews.data.ArticleRepository
import javax.inject.Inject
import javax.inject.Singleton

sealed class ArticlesState {
    object Loading : ArticlesState()
    class Result(val data: List<Article>, val append: Boolean = false): ArticlesState()
    class Error(val error: String): ArticlesState()
}

@Singleton
class ArticlesViewModel @Inject constructor(
    val repository: ArticleRepository
) {
    val articles: PublishSubject<ArticlesState> = PublishSubject.create()

    fun getArticle(articleId: Int): Single<Article> {
        return Single.create<Article> {
            it.onSuccess(repository.getArticle(articleId))
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun loadArticles(reload: Boolean = false) {
        articles.onNext(ArticlesState.Loading)
        repository.getArticles(reload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                articles.onNext(ArticlesState.Result(it))
            },{
                articles.onNext(ArticlesState.Error(it.message ?: "Couldn't load articles"))
            })
    }

    fun loadMoreArticles() {
        articles.onNext(ArticlesState.Loading)
        repository.getMoreArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                articles.onNext(ArticlesState.Result(it, true))
            },{
                articles.onNext(ArticlesState.Error(it.message ?: "Couldn't load articles"))
            })
    }
}