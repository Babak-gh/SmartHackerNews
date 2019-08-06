package mobi.artapps.smarthackernews.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import mobi.artapps.smarthackernews.model.local.entity.News
import mobi.artapps.smarthackernews.model.remote.HackerNewsService
import mobi.artapps.smarthackernews.model.remote.ServiceGenerator
import mobi.artapps.smarthackernews.model.remote.entity.FeedItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsDataSource : PageKeyedDataSource<Int, News>() {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
        loadPage(1) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        networkState.postValue(NetworkState.LOADING)
        val page = params.key
        loadPage(page) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
    }


    private fun loadPage(page: Int, callBack: (List<News>) -> Unit) {

        val hackerNewsService =
            ServiceGenerator.createDefaultService(HackerNewsService::class.java, ServiceGenerator.baseURL)

        networkState.postValue(NetworkState.LOADING)
        if (page == 1) initialLoad.postValue(NetworkState.LOADING)

        val call = hackerNewsService.getNews(page.toString())
        call.enqueue(object : Callback<List<FeedItem>> {
            override fun onFailure(call: Call<List<FeedItem>>, t: Throwable) {
                Log.d("BABAK", "fail to get data")
                val error = NetworkState.error(t.message ?: "unknown error")
                networkState.postValue(error)
                if (page == 1) initialLoad.postValue(error)
                /*_networkErrors.postValue(t.message ?: "unknown error")
                isRequestInProgress = false*/
            }

            override fun onResponse(call: Call<List<FeedItem>>, response: Response<List<FeedItem>>) {
                Log.d("BABAK", "got a response $response")
                if (response.isSuccessful) {
                    val newsFeeds = response.body() ?: emptyList()

                    val news: MutableList<News> = mutableListOf()
                    for (newsFeed in newsFeeds) {
                        val newsItem = News(
                            null,
                            newsFeed.id,
                            newsFeed.title,
                            newsFeed.points,
                            newsFeed.user,
                            newsFeed.time,
                            newsFeed.time_ago,
                            newsFeed.comments_count,
                            newsFeed.type,
                            newsFeed.url,
                            newsFeed.domain
                        )
                        news += newsItem
                    }

                    networkState.postValue(NetworkState.LOADED)
                    if (page == 1) initialLoad.postValue(NetworkState.LOADED)

                    callBack(news)


                    /*if (!isFromPullDown){
                        lastRequestedPage++
                    }
                    isRequestInProgress = false*/
                } else {
                    /*_networkErrors.postValue(response.errorBody()?.string() ?: "Unknown error")
                    isRequestInProgress = false*/
                    val error = NetworkState.error(response.errorBody()?.string() ?: "unknown error")
                    networkState.postValue(error)
                    if (page == 1) initialLoad.postValue(error)
                }
            }


        })
    }
}