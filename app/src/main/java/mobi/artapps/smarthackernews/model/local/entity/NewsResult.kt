package mobi.artapps.smarthackernews.model.local.entity

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import mobi.artapps.smarthackernews.data.NetworkState

data class NewsResult(
    val data: LiveData<PagedList<News>>,
    val networkState: LiveData<NetworkState>,
    val refreshState: LiveData<NetworkState>,
    val refresh: () -> Unit
)