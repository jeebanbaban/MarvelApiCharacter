package marvel.api.marvelcharacters.ui.characterlist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.local.dao.ResultDao
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.repository.CharacterListRepository
import javax.inject.Inject

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
@ExperimentalPagingApi
@HiltViewModel
class CharacterListViewModel @Inject constructor(val resultDao: ResultDao): ViewModel() {

    fun getCharacters(): Flow<PagingData<Results>>{
        return CharacterListRepository.getMarvelCharacters().cachedIn(viewModelScope)
    }

    fun getMarvels(): Flow<PagingData<Results>>{
        return CharacterListRepository.getMarvels(resultDao).cachedIn(viewModelScope)
    }
}