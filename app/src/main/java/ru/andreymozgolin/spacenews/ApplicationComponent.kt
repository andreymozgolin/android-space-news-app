package ru.andreymozgolin.spacenews

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.andreymozgolin.spacenews.articles.ArticlesFragment
import ru.andreymozgolin.spacenews.di.DbModule
import ru.andreymozgolin.spacenews.di.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DbModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(activity: MainActivity)
    fun inject(activity: ArticlesFragment)
}