package ru.andreymozgolin.spacenews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.andreymozgolin.spacenews.articles.ArticlesFragment

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container, ArticlesFragment())
                .commit()
        }
    }
}