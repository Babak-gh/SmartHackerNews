package mobi.artapps.smarthackernews.model.local.entity

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

class NewsResult(
    val data: LiveData<PagedList<News>>,
    val networkErrors: LiveData<String>
)