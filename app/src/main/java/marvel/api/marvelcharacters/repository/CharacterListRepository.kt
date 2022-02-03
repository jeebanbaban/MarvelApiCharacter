package marvel.api.marvelcharacters.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.local.dao.ResultDao
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.ui.characterlist.paging.CharacterMediator
import marvel.api.marvelcharacters.ui.characterlist.paging.MarvelMediator
import marvel.api.marvelcharacters.ui.characterlist.paging.ResultMediator
import javax.inject.Inject

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


    fun getMarvels(resultDao: ResultDao): Flow<PagingData<Results>> {
        return Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = true, prefetchDistance = 15),
            remoteMediator = MarvelMediator()
        ){
            resultDao.getAllResults()
        }.flow
    }


}