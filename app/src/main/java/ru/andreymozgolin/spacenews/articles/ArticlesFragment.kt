package ru.andreymozgolin.spacenews.articles

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.SpaceNewsApplication
import javax.inject.Inject

class ArticlesFragment: Fragment(R.layout.fragment_articles) {
    @Inject lateinit var viewModel: ArticlesViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as SpaceNewsApplication).component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            recyclerView = findViewById(R.id.articles_recycler)
            recyclerView.adapter = ArticlesAdapter(listOf())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.articles.subscribe {
            when (it) {
                is ArticlesState.Loading ->
                    Toast.makeText(
                        context,
                        "Loading articles",
                        Toast.LENGTH_SHORT
                    ).show()
                is ArticlesState.Result -> {
                    (recyclerView.adapter as ArticlesAdapter).refreshArticles(it.data)
                    Toast.makeText(
                        context,
                        "Data loaded ${it.data.size}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ArticlesState.Error -> Toast.makeText(context, it.error, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}