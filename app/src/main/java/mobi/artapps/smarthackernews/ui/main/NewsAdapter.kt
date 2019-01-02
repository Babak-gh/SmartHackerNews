package mobi.artapps.smarthackernews.ui.main

import android.arch.paging.PagedListAdapter
import android.content.Intent
import android.net.Uri
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mobi.artapps.smarthackernews.R
import mobi.artapps.smarthackernews.model.local.entity.News

class NewsAdapter : PagedListAdapter<News, RecyclerView.ViewHolder>(NEWS_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return NewsViewHolder.create(parent)

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val newsItem = getItem(position)
        if (newsItem != null) {
            (holder as NewsViewHolder).bind(newsItem)
        }

    }


    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.news_view_item_title)

        private var news: News? = null

        init {
            view.setOnClickListener {
                news?.url?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view.context.startActivity(intent)
                }
            }
        }

        fun bind(news: News?) {
            if (news == null) {
                val resources = itemView.resources
                //name.text = resources.getString(R.string.loading)

            } else {
                showRepoData(news)
            }
        }

        private fun showRepoData(news: News) {
            this.news = news
            name.text = news.title
        }

        companion object {
            fun create(parent: ViewGroup): NewsViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_view_item, parent, false)
                return NewsViewHolder(view)
            }
        }
    }

    companion object {
        private val NEWS_COMPARATOR = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem.newsId == newItem.newsId


            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem == newItem

        }
    }
}