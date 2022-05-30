package ru.andreymozgolin.spacenews.articles

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.SpaceNewsApplication
import javax.inject.Inject

private const val TAG = "ArticlesFragment"

class ArticlesFragment: Fragment() {
    @Inject lateinit var viewModel: ArticlesViewModel
    private var subscriptions: CompositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: ProgressBar
    private var callbacks: Callbacks? = null
    private var isLoading = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as SpaceNewsApplication).component.inject(this)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        observeViewModel()
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
        recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager!!
                if (dy > 0 && layoutManager.findViewByPosition(layoutManager.itemCount - 1) != null && !isLoading) {
                    isLoading = true
                    viewModel.loadMoreArticles()
                }
            }
        })

        viewModel.loadArticles()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_refresh -> {
            viewModel.loadArticles(reload = true)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun observeViewModel() {
        subscriptions.add(viewModel.articles.subscribe {
            when (it) {
                is ArticlesState.Loading -> {
                    isLoading = true
                    loadingView.visibility = ProgressBar.VISIBLE
                    Log.d(TAG, "Loading articles...")
                }
                is ArticlesState.Result -> {
                    Log.d(TAG, "Received new articles.")
                    isLoading = false
                    loadingView.visibility = ProgressBar.INVISIBLE
                    val adapter = (recyclerView.adapter as ArticlesAdapter)
                    if (it.append)
                        adapter.addArticles(it.data)
                    else
                        adapter.refreshArticles(it.data)
                }
                is ArticlesState.Error -> {
                    isLoading = false
                    loadingView.visibility = ProgressBar.INVISIBLE
                    Log.e(TAG, "Couldn't load articles. Error details: ${it.error}")
                    Toast.makeText(context, "Couldn't load articles.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    interface Callbacks {
        fun onArticleSelected(articleId: Int)
    }

}