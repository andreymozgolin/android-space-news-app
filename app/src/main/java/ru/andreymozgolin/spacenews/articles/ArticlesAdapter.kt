package ru.andreymozgolin.spacenews.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.data.Article

class ArticlesAdapter(var articles: List<Article>, val callbacks: ArticlesFragment.Callbacks?): RecyclerView.Adapter<ArticlesAdapter.ArticleHolder>() {

    inner class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var article: Article
        private val image = itemView.findViewById<ImageView>(R.id.article_item_image)
        private val title = itemView.findViewById<TextView>(R.id.article_item_title)

        init {
            itemView.setOnClickListener {
                this@ArticlesAdapter.callbacks?.onArticleSelected(article.id)
            }
        }

        fun bind(article: Article) {
            this.article = article
            title.text = article.title
            Picasso.get().load(article.imageUrl).fit().centerCrop().into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        return ArticleHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    fun refreshArticles(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }
}