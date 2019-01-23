package mobi.artapps.smarthackernews.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.PagedList
import mobi.artapps.smarthackernews.data.NewsRepository
import mobi.artapps.smarthackernews.model.local.entity.News
import mobi.artapps.smarthackernews.model.local.entity.NewsResult

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mNewsRepository = NewsRepository(application)

    private val queryLiveData = MutableLiveData<String>()
    private val newsResult: LiveData<NewsResult> = Transformations.map(queryLiveData) {
        mNewsRepository.getAllNewsFromNetworkOrDB()
    }

    val mAllNews: LiveData<PagedList<News>> = Transformations.switchMap(newsResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(newsResult) { it -> it.networkErrors }

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun invalidateDataSource() {
        mNewsRepository.invalidateDataSource()
    }

}
