package mobi.artapps.smarthackernews.data

import android.app.Application
import android.arch.paging.LivePagedListBuilder
import android.os.AsyncTask
import mobi.artapps.smarthackernews.model.local.AppDataBase
import mobi.artapps.smarthackernews.model.local.NewsDAO
import mobi.artapps.smarthackernews.model.local.entity.News
import mobi.artapps.smarthackernews.model.local.entity.NewsResult

class NewsRepository(application: Application) {


    private val db = AppDataBase.getInstance(application)
    private val newsDao = db.newsDao()
    private val mAllNews = newsDao.getAll()
    private var boundaryCallback : NewsBoundaryCallback? = null


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

    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

    fun getAllNews() = mAllNews

    fun deleteDatabaseData() = DeleteAsyncTask(newsDao).execute()

    fun insert(news: List<News>) {
        InsertAsyncTask(newsDao).execute(news)
    }

    private class InsertAsyncTask(mAsyncTaskDao: NewsDAO) : AsyncTask<List<News>, Void, Unit>() {

        private val mAsyncTaskDao = mAsyncTaskDao


        override fun doInBackground(vararg params: List<News>?) {

            mAsyncTaskDao.insertAll(params[0]!!)

        }

    }

    private class DeleteAsyncTask(mAsyncTaskDao: NewsDAO) : AsyncTask<Void, Void, Unit>() {

        private val mAsyncTaskDao = mAsyncTaskDao


        override fun doInBackground(vararg params: Void?) {

            mAsyncTaskDao.deleteAll()

        }

    }

}