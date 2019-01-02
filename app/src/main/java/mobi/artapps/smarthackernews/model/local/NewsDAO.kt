package mobi.artapps.smarthackernews.model.local

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import mobi.artapps.smarthackernews.model.local.entity.News

@Dao
interface NewsDAO {

    @Query("SELECT * FROM news")
    fun getAll(): DataSource.Factory<Int, News>

    @Query("SELECT * FROM news WHERE newsId IN (:newsIds)")
    fun loadAllByIds(newsIds: IntArray): List<News>

    @Query("SELECT * FROM news WHERE newsId == (:newsId)")
    fun loadById(newsId: Int): News

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): News

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<News>)

    @Delete
    fun delete(news: News)


}