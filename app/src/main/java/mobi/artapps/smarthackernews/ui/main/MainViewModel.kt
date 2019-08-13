package mobi.artapps.smarthackernews.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import mobi.artapps.smarthackernews.data.NetworkState
import mobi.artapps.smarthackernews.data.NewsRepository
import mobi.artapps.smarthackernews.model.local.entity.News
import mobi.artapps.smarthackernews.model.local.entity.NewsResult
import mobi.artapps.smarthackernews.model.remote.entity.NewsType

class MainViewModel(private val mNewsRepository: NewsRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<NewsType>()
    private val newsResult: LiveData<NewsResult> = Transformations.map(queryLiveData) {
        mNewsRepository.getAllNewsFromNetworkOrDB(it)
    }

    val mAllNews: LiveData<PagedList<News>> = Transformations.switchMap(newsResult) { it.data }
    val networkErrors: LiveData<NetworkState> = Transformations.switchMap(newsResult) { it.networkState }

    fun searchRepo(queryString: NewsType) {
        queryLiveData.postValue(queryString)
    }

    fun invalidateDataSource() {
        newsResult.value?.refresh?.invoke()
    }

}
