package mobi.artapps.smarthackernews.data

import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import mobi.artapps.smarthackernews.model.local.entity.NewsResult
import mobi.artapps.smarthackernews.model.remote.entity.NewsType

class NewsRepository {

    private val dataSourceFactory = NewsDataSourceFactory()

    fun getAllNewsFromNetworkOrDB(newsType: NewsType): NewsResult {

        dataSourceFactory.newsType = newsType
        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = NewsBoundaryCallback()

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, pagedListConfig())
            .setBoundaryCallback(boundaryCallback)
            .build()

        val refreshState = Transformations.switchMap(dataSourceFactory.sourceLiveData) {
            it.initialLoad
        }

        // Get the network errors exposed by the boundary callback
        return NewsResult(data,
            Transformations.switchMap(dataSourceFactory.sourceLiveData) {
                it.networkState
            },
            refreshState
        ) {
            dataSourceFactory.sourceLiveData.value?.invalidate()
        }
    }

    private fun pagedListConfig() = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(15)
        .build()

}