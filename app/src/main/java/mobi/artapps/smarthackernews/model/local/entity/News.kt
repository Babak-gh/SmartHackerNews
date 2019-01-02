package mobi.artapps.smarthackernews.model.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "news", indices = arrayOf(
        Index(
            value = ["newsId"],
            unique = true
        )
    )
)

data class News(
    @PrimaryKey var _id: Int?, var newsId: Int, var title: String, var points: Int?,
    var user: String?, var time: Int, var time_ago: String,
    var comments_count: Int, var type: String, var url: String?,
    var domain: String?
)