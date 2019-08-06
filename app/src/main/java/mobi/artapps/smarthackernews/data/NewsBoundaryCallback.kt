package mobi.artapps.smarthackernews.data

import android.util.Log
import androidx.paging.PagedList
import mobi.artapps.smarthackernews.model.local.entity.News

class NewsBoundaryCallback : PagedList.BoundaryCallback<News>() {

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    override fun onZeroItemsLoaded() {
        Log.d("RepoBoundaryCallback", "onZeroItemsLoaded")
        //requestAndSaveData(false)
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    override fun onItemAtEndLoaded(itemAtEnd: News) {
        Log.d("RepoBoundaryCallback", "onItemAtEndLoaded")
        //requestAndSaveData(false)
    }


/*    private fun requestAndSaveData(isFromPullDown:Boolean) {
        if (isRequestInProgress) return

        isRequestInProgress = true


        val hackerNewsService =
            ServiceGenerator.createDefaultService(HackerNewsService::class.java, ServiceGenerator.baseURL)
        val call = hackerNewsService.getNews(if (isFromPullDown) "1" else lastRequestedPage.toString())
        call.enqueue(object : Callback<List<FeedItem>> {
            override fun onFailure(call: Call<List<FeedItem>>, t: Throwable) {
                Log.d("BABAK", "fail to get data")
                _networkErrors.postValue(t.message ?: "unknown error")
                isRequestInProgress = false
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

                    save(news)
                    if (!isFromPullDown){
                        lastRequestedPage++
                    }
                    isRequestInProgress = false
                } else {
                    _networkErrors.postValue(response.errorBody()?.string() ?: "Unknown error")
                    isRequestInProgress = false
                }
            }


        })

    }*/
}