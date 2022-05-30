package ru.andreymozgolin.spacenews.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.andreymozgolin.spacenews.data.Article

interface SpaceNewsService {

    @GET("articles")
    fun getArticles(
        @Query("id_lt") fromId: Int? = null,
        @Query("_limit") limit: Int = 10
    ): Call<List<Article>>
}