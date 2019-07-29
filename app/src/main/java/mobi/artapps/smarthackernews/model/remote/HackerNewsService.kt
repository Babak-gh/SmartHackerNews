package mobi.artapps.smarthackernews.model.remote

import mobi.artapps.smarthackernews.model.remote.entity.FeedItem
import mobi.artapps.smarthackernews.model.remote.entity.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface HackerNewsService {

    @GET("news/{pageNumber}.json")
    fun getNews(@Path("pageNumber") pageNumber: String): Call<List<FeedItem>>

    @GET("newest/{pageNumber}.json")
    fun getNewest(@Path("pageNumber") pageNumber: String): Call<List<FeedItem>>

    @GET("ask/{pageNumber}.json")
    fun getAsk(@Path("pageNumber") pageNumber: String): Call<List<FeedItem>>

    @GET("show/{pageNumber}.json")
    fun getShow(@Path("pageNumber") pageNumber: String): Call<List<FeedItem>>

    @GET("jobs/{pageNumber}.json")
    fun getJobs(@Path("pageNumber") pageNumber: String): Call<List<FeedItem>>

    @GET("user/{userName}.json")
    fun getUser(@Path("userName") userName: String): Call<User>

}