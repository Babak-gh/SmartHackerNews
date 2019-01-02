package mobi.artapps.smarthackernews.model.remote

import mobi.artapps.smarthackernews.model.remote.entity.FeedItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface HackerNewsService {

    @GET("news/{pageNumber}.json")
    fun getNews(@Path("pageNumber") pageNumber: String): Call<List<FeedItem>>

}