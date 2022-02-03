package marvel.api.marvelcharacters.ui.characterlist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.repository.CharacterListRepository

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
@ExperimentalPagingApi
class CharacterListViewModel(): ViewModel() {

    fun getCharacters(): Flow<PagingData<Results>>{
        return CharacterListRepository.getMarvelCharacters().cachedIn(viewModelScope)
    }

    fun getResults(apiService: ApiService, marvelDatabase: MarvelDatabase): Flow<PagingData<Results>>{
        return CharacterListRepository.getResults(apiService, marvelDatabase).cachedIn(viewModelScope)
    }

    fun getCharacters(apiService: ApiService, marvelDatabase: MarvelDatabase): Flow<PagingData<Results>>{
        return CharacterListRepository.getCharcters(apiService, marvelDatabase).cachedIn(viewModelScope)
    }

    fun getMarvels(apiService: ApiService, marvelDatabase: MarvelDatabase): Flow<PagingData<Results>>{
        return CharacterListRepository.getMarvels(apiService, marvelDatabase).cachedIn(viewModelScope)
    }
}