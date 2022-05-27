package ru.andreymozgolin.spacenews.articles

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.SpaceNewsApplication
import javax.inject.Inject

class ArticlesFragment: Fragment() {
    @Inject lateinit var viewModel: ArticlesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: ProgressBar
    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as SpaceNewsApplication).component.inject(this)
        callbacks = context as Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)

        loadingView = view.findViewById(R.id.articles_loading)
        recyclerView = view.findViewById(R.id.articles_recycler)
        recyclerView.adapter = ArticlesAdapter(listOf(), callbacks)

        viewModel.articles.subscribe {
            when (it) {
                is ArticlesState.Loading ->
                    loadingView.visibility = ProgressBar.VISIBLE
                is ArticlesState.Result -> {
                    loadingView.visibility = ProgressBar.INVISIBLE
                    (recyclerView.adapter as ArticlesAdapter).refreshArticles(it.data)
                }
                is ArticlesState.Error -> {
                    loadingView.visibility = ProgressBar.INVISIBLE
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    interface Callbacks {
        fun onArticleSelected(articleId: Int)
    }

}