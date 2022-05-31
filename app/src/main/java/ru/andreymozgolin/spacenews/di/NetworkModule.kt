package ru.andreymozgolin.spacenews.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.andreymozgolin.spacenews.api.SpaceNewsService
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideSpaceNewsService(): SpaceNewsService {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create()
        return Retrofit.Builder()
            .baseUrl("https://api.spaceflightnewsapi.net/v3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SpaceNewsService::class.java)
    }
}