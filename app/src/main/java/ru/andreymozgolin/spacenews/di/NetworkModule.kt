package ru.andreymozgolin.spacenews.di

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
        return Retrofit.Builder()
            .baseUrl("https://api.spaceflightnewsapi.net/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpaceNewsService::class.java)
    }
}