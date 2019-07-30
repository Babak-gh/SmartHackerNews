package mobi.artapps.smarthackernews.model.local.entity

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

class NewsResult(
    val data: LiveData<PagedList<News>>,
    val networkErrors: LiveData<String>
)