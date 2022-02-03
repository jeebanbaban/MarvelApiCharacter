package marvel.api.marvelcharacters.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.ui.characterlist.paging.CharacterMediator
import marvel.api.marvelcharacters.ui.characterlist.paging.MarvelMediator
import marvel.api.marvelcharacters.ui.characterlist.paging.ResultMediator

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
@ExperimentalPagingApi
object CharacterListRepository {
    private const val NETWORK_PAGE_SIZE = 10

    fun getMarvelCharacters(): Flow<PagingData<Results>> {
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = true),
            pagingSourceFactory = { CharacterListDataSource() }
        ).flow
    }


    fun getResults(apiService: ApiService, marvelDatabase: MarvelDatabase): Flow<PagingData<Results>> {
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = ResultMediator(apiService, marvelDatabase)
        ){
            marvelDatabase.getResultDao().getAllResults()
        }.flow
    }

    fun getCharcters(apiService: ApiService, marvelDatabase: MarvelDatabase): Flow<PagingData<Results>> {
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = true, prefetchDistance = 12),
            remoteMediator = CharacterMediator(apiService, marvelDatabase)
        ){
            marvelDatabase.getResultDao().getAllResults()
        }.flow
    }

    fun getMarvels(apiService: ApiService, marvelDatabase: MarvelDatabase): Flow<PagingData<Results>> {
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = true, prefetchDistance = 15),
            remoteMediator = MarvelMediator(apiService, marvelDatabase)
        ){
            marvelDatabase.getResultDao().getAllResults()
        }.flow
    }


}