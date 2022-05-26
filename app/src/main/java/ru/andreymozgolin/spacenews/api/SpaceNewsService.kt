package ru.andreymozgolin.spacenews.api

import retrofit2.Call
import retrofit2.http.GET
import ru.andreymozgolin.spacenews.data.Article

interface SpaceNewsService {

    @GET("articles")
    fun getArticles(): Call<List<Article>>
}