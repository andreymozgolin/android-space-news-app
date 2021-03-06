package ru.andreymozgolin.spacenews.articles

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.SpaceNewsApplication
import javax.inject.Inject

private const val TAG = "ArticlesFragment"

class ArticlesFragment: Fragment() {
    @Inject lateinit var viewModel: ArticlesViewModel
    private lateinit var subscriptions: CompositeDisposable
    private lateinit var adapter: ArticlesAdapter
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
        adapter = ArticlesAdapter(listOf(), callbacks)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)

        loadingView = view.findViewById(R.id.articles_loading)
        recyclerView = view.findViewById(R.id.articles_recycler)
        recyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.article_columns))
        recyclerView.adapter = adapter
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

        observeViewModel()
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
        subscriptions = CompositeDisposable()
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
                    adapter.refreshArticles(if (it.changes) adapter.articles + it.data else it.data)
                }
                is ArticlesState.Error -> {
                    isLoading = false
                    loadingView.visibility = ProgressBar.INVISIBLE
                    Log.e(TAG, "Couldn't load articles. Error details: ${it.error}",it.throwable)
                    Toast.makeText(context, "Couldn't load articles.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
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