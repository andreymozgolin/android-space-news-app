package ru.andreymozgolin.spacenews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.andreymozgolin.spacenews.articles.ArticleDetailFragment
import ru.andreymozgolin.spacenews.articles.ArticlesFragment
import ru.andreymozgolin.spacenews.receivers.AlarmReceiver

class MainActivity: AppCompatActivity(), ArticlesFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AlarmReceiver.setAlarm(this)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container, ArticlesFragment())
                .commit()
        }
    }

    override fun onArticleSelected(articleId: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment_container, ArticleDetailFragment.create(articleId))
            .addToBackStack(null)
            .commit()
    }
}