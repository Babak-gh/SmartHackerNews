package mobi.artapps.smarthackernews.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import mobi.artapps.smarthackernews.model.local.entity.News

class NewsDataSourceFactory : DataSource.Factory<Int, News>() {

    val sourceLiveData = MutableLiveData<NewsDataSource>()
    override fun create(): DataSource<Int, News> {
        val source = NewsDataSource()
        sourceLiveData.postValue(source)
        return source
    }
}