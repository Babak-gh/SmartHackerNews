package mobi.artapps.smarthackernews.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import mobi.artapps.smarthackernews.data.NewsRepository
import mobi.artapps.smarthackernews.model.local.entity.News
import mobi.artapps.smarthackernews.model.local.entity.NewsResult

class MainViewModel(private val mNewsRepository: NewsRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    private val newsResult: LiveData<NewsResult> = Transformations.map(queryLiveData) {
        mNewsRepository.getAllNewsFromNetworkOrDB()
    }

    val mAllNews: LiveData<PagedList<News>> = Transformations.switchMap(newsResult) { it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(newsResult) { it.networkErrors }

    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun invalidateDataSource() {
        mNewsRepository.invalidateDataSource()
    }

}
