package mobi.artapps.smarthackernews.data

import android.app.Application
import androidx.paging.LivePagedListBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mobi.artapps.smarthackernews.model.local.AppDataBase
import mobi.artapps.smarthackernews.model.local.entity.News
import mobi.artapps.smarthackernews.model.local.entity.NewsResult

class NewsRepository(application: Application) {


    private val db = AppDataBase.getInstance(application)
    private val newsDao = db.newsDao()
    private val mAllNews = newsDao.getAll()
    private var boundaryCallback : NewsBoundaryCallback? = null
    private val ioScope = CoroutineScope(Dispatchers.IO)

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

    fun invalidateDataSource(){
        boundaryCallback?.onPullDown()
    }

    fun getAllNewsFromNetworkOrDB(): NewsResult {
        // Get data source factory from the local cache
        val dataSourceFactory = getAllNews()

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        boundaryCallback = NewsBoundaryCallback ({ news ->
            insert(news)
        },{ deleteDatabaseData()} )

        val networkErrors = boundaryCallback?.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return NewsResult(data, networkErrors!!)
    }


    private fun getAllNews() = mAllNews

    private fun deleteDatabaseData() = ioScope.launch {
        newsDao.deleteAll()
    }

    private fun insert(news: List<News>) = ioScope.launch {
        newsDao.insertAll(news)
    }

}