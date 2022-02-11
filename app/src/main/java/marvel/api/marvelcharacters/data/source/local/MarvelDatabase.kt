package marvel.api.marvelcharacters.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import marvel.api.marvelcharacters.data.model.RemoteKeys
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.dao.RemoteKeysDao
import marvel.api.marvelcharacters.data.source.local.dao.ResultDao
import marvel.api.marvelcharacters.utils.AppConstant.Companion.DB_NAME

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
@Database(entities = [Results::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class MarvelDatabase: RoomDatabase() {

    companion object{
        @Volatile
        private var INSTANCE: MarvelDatabase? = null

        fun getInstance(context: Context): MarvelDatabase = INSTANCE ?: synchronized(this){ INSTANCE ?: createDatabase(context).also { INSTANCE = it } }

        private fun createDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, MarvelDatabase::class.java, DB_NAME).allowMainThreadQueries().build()
    }

    abstract fun getResultDao(): ResultDao

    abstract fun getRemoteKeyDao(): RemoteKeysDao
}