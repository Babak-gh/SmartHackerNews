package mobi.artapps.smarthackernews.model.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import mobi.artapps.smarthackernews.model.local.entity.News

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun newsDao(): NewsDAO


    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java, "hn"
            )
                .build()

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}