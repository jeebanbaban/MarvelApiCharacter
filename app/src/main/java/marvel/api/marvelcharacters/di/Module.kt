package marvel.api.marvelcharacters.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.local.dao.RemoteKeysDao
import marvel.api.marvelcharacters.data.source.local.dao.ResultDao
import marvel.api.marvelcharacters.data.source.remote.ApiClient
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.data.source.remote.Services
import marvel.api.marvelcharacters.utils.AppConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Singleton
    @Provides
    fun provideMarvelDatabase(@ApplicationContext context: Context): MarvelDatabase {
        return Room.databaseBuilder(
            context, MarvelDatabase::class.java,
            AppConstant.DB_NAME
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideResultDao(marvelDatabase: MarvelDatabase): ResultDao {
        return marvelDatabase.getResultDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeyDao(marvelDatabase: MarvelDatabase): RemoteKeysDao {
        return marvelDatabase.getRemoteKeyDao()
    }

   @Singleton
   @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level.apply {
                HttpLoggingInterceptor.Level.HEADERS
                HttpLoggingInterceptor.Level.BODY
            }
        }).connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Services.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService{
        return retrofit.create(ApiService::class.java)
    }

}