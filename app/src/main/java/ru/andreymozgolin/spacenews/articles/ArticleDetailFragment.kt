package ru.andreymozgolin.spacenews.articles

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.SpaceNewsApplication
import ru.andreymozgolin.spacenews.data.Article
import javax.inject.Inject

private const val ARG_ARTICLE_ID = "article_id"

class ArticleDetailFragment: Fragment(R.layout.fragment_article_detail) {
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var summary: TextView
    private lateinit var published: TextView
    private lateinit var site: TextView
    private lateinit var link: Button

    @Inject
    lateinit var viewModel: ArticlesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as SpaceNewsApplication).component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article_detail, container, false)

        //Init of views
        image = view.findViewById(R.id.article_detail_image)
        title = view.findViewById(R.id.article_detail_title)
        summary = view.findViewById(R.id.article_detail_summary)
        published = view.findViewById(R.id.article_detail_published)
        site = view.findViewById(R.id.article_detail_source)
        link = view.findViewById(R.id.article_detail_link)

        //Getting article by id and refresh view
        val articleId = arguments?.getInt(ARG_ARTICLE_ID)!!
        viewModel.getArticle(articleId).subscribe(this::refreshView)

        return view
    }

    private fun refreshView(article: Article) {
        Picasso.get().load(article.imageUrl).into(image)
        title.text = article.title
        summary.text = article.summary
        published.text = getString(R.string.published_at, article.publishedAt)
        site.text = getString(R.string.news_site, article.newsSite)
        link.apply {
            text = article.newsSite
            setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.url)))
            }
        }
    }

    companion object {
        fun create(articleId: Int): ArticleDetailFragment {
            return ArticleDetailFragment().apply {
                arguments = bundleOf(ARG_ARTICLE_ID to articleId)
            }
        }
    }
}