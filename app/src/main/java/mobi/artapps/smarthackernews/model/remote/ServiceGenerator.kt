package mobi.artapps.smarthackernews.model.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {


    val baseURL = "https://api.hnpwa.com/v0/"
    private val httpLogger = HttpLoggingInterceptor()
    private val retrofitBuilder = Retrofit.Builder()


    fun <S> createDefaultService(serviceClass: Class<S>, baseURL: String): S {


        httpLogger.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = newOkHttpBuilder()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = retrofitBuilder
            .client(okHttpBuilder.build())
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(serviceClass)
    }

    private fun newOkHttpBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        if (httpLogger.level != HttpLoggingInterceptor.Level.NONE) {
            builder.addInterceptor(httpLogger)
        }
        return builder
    }


}