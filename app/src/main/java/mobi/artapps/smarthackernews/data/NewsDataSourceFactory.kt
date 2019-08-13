package mobi.artapps.smarthackernews.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import mobi.artapps.smarthackernews.model.local.entity.News
import mobi.artapps.smarthackernews.model.remote.entity.NewsType

class NewsDataSourceFactory : DataSource.Factory<Int, News>() {

    var newsType: NewsType = NewsType.NEWS

    val sourceLiveData = MutableLiveData<NewsDataSource>()
    override fun create(): DataSource<Int, News> {
        val source = NewsDataSource(newsType)
        sourceLiveData.postValue(source)
        return source
    }
}