package mobi.artapps.smarthackernews.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
        private val nameTextView: TextView = view.findViewById(R.id.news_view_item_title)
        private val timeAgoTextView: TextView = view.findViewById(R.id.news_view_item_time_ago)
        private val userTextView: TextView = view.findViewById(R.id.news_view_item_user)
        private val linkTextView: TextView = view.findViewById(R.id.news_view_item_link)
        private val commentTextView: TextView = view.findViewById(R.id.news_view_item_comment)
        private val pointsTextView: TextView = view.findViewById(R.id.news_view_item_points)
        private val menuImageView: ImageView = view.findViewById(R.id.news_view_item_menu_imageView)

        private var news: News? = null

        init {
            view.setOnClickListener {

            }

            view.setOnLongClickListener {
                openContextMenu()
                true
            }

            menuImageView.setOnClickListener {
                openContextMenu()
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
            nameTextView.text = news.title
            timeAgoTextView.text = news.time_ago
            userTextView.text = "by: ${news.user}"
            linkTextView.text = news.domain
            commentTextView.text = news.comments_count.toString()
            pointsTextView.text = "${news.points}p"

        }

        companion object {
            fun create(parent: ViewGroup): NewsViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_view_item, parent, false)
                return NewsViewHolder(view)
            }
        }

        private fun openContextMenu() {

            val popup = PopupMenu(itemView.context, menuImageView)
            popup.inflate(R.menu.main_list_item_context_menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.context_menu_browser -> openInBrowser()
                    R.id.context_menu_copy -> copyLinkToClipBoard()
                    R.id.context_menu_share -> shareLink()
                }
                true
            }
            popup.show()
        }

        private fun shareLink() {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, news?.title)
            i.putExtra(Intent.EXTRA_TEXT, String.format("%s - %s", news?.title, news?.url))
            startActivity(itemView.context, Intent.createChooser(i, "Share via"), null)
        }

        private fun copyLinkToClipBoard() {
            val clipboard = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("link", news?.url)
            clipboard.primaryClip = clip
        }

        private fun openInBrowser() {
            news?.url?.let { url ->

                val customTabsIntentBuilder = CustomTabsIntent.Builder()
                customTabsIntentBuilder.setToolbarColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                val customTabsIntent = customTabsIntentBuilder.build()
                customTabsIntent.launchUrl(itemView.context, Uri.parse(url))

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