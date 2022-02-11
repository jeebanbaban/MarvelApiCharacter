package marvel.api.marvelcharacters.ui.characterlist.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import marvel.api.marvelcharacters.data.model.RemoteKeys
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.remote.ApiClient
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.utils.AppConfig
import marvel.api.marvelcharacters.utils.AppConstant
import retrofit2.HttpException
import java.io.IOException
import java.sql.Timestamp

/**
 * Created by Jeeban Bagdi on 2/1/2022.
 */
@ExperimentalPagingApi
class CharacterMediator(val apiService: ApiService, val marvelDatabase: MarvelDatabase) :
    RemoteMediator<Int, Results>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Results>
    ): MediatorResult {

        return try {
            // 1
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    getRemoteKeys()
                }
            }

            // 2
            val ts = Timestamp(System.currentTimeMillis()).time.toString()
            val responsee = ApiClient.getService().getMarvelCharacters(
                ts = ts,
                apikey = AppConstant.API_KEY,
                hash = AppConfig.getMD5Hash(ts),
                offset = loadKey?.nextKey?.toString() ?: "1",
                limit = state.config.pageSize.toString()
            )
            val responseResults = responsee.body()?.data?.results
            val endOfPagination = responseResults.isNullOrEmpty() || responseResults.size < state.config.pageSize
            if (null != responseResults){
                // 3
                if (loadType == LoadType.REFRESH) {
                    marvelDatabase.getResultDao().clearAllResults()
                    marvelDatabase.getRemoteKeyDao().clearRemoteKeys()
                }
                marvelDatabase.withTransaction {
                    val prevKey = if (responsee.body()?.data?.offset == 1) null else responsee.body()?.data?.offset!! - 1
                    val nextKey = if (endOfPagination) null else responsee.body()?.data?.offset!! + 1
                    marvelDatabase.getRemoteKeyDao().saveRemoteKeys(RemoteKeys("0", prevKey, nextKey))
                    marvelDatabase.getResultDao().insertAll(responseResults)
                }
            }
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeys(): RemoteKeys? {
        return marvelDatabase.getRemoteKeyDao().getRemoteKeys().firstOrNull()
    }

}