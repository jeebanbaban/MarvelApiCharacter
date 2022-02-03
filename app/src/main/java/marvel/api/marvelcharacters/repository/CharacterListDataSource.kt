package marvel.api.marvelcharacters.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.AndroidEntryPoint
import marvel.api.marvelcharacters.data.model.Data
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.remote.ApiClient
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.utils.AppConfig
import marvel.api.marvelcharacters.utils.AppConstant
import java.sql.Timestamp
import javax.inject.Inject

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
class CharacterListDataSource: PagingSource<Int, Results>() {

    @Inject lateinit var apiService: ApiService

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Results> {
        try {
            val pageIndex = params.key ?: 1
            val ts = Timestamp(System.currentTimeMillis()).time.toString()
            val response = ApiClient.getService().getMarvelCharacters(
                ts = ts,
                apikey = AppConstant.API_KEY,
                hash = AppConfig.getMD5Hash(ts),
                offset = pageIndex.toString(),
                limit = "10"
            )
            if (response.isSuccessful){
                val responseResult =  response.body()?.data?.results ?: emptyList()
                val prevKey = if (pageIndex == 1) null else pageIndex - 1
                return LoadResult.Page(
                    data = responseResult,
                    prevKey = prevKey,
                    nextKey = if (responseResult.isNullOrEmpty()) null else pageIndex + 1
                )
            }else{
                return LoadResult.Error(Throwable("Response not getting!"))
            }
        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Results>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}