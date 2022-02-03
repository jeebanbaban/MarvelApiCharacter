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
 * https://medium.com/swlh/paging3-recyclerview-pagination-made-easy-333c7dfa8797
 */
@ExperimentalPagingApi
class ResultMediator(val apiService: ApiService,val marvelDatabase: MarvelDatabase) :
    RemoteMediator<Int, Results>()  {

    private val DEFAULT_PAGE_INDEX = 1

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Results>): MediatorResult {
//        val pageKeyData = getKeyPageData(loadType, state)
//        val page = when (pageKeyData) {
//            is MediatorResult.Success -> {
//                return pageKeyData
//            }
//            else -> {
//                pageKeyData as Int
//            }
//        }

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        try {
            val ts = Timestamp(System.currentTimeMillis()).time.toString()
            val response = ApiClient.getService().getMarvelCharacters(
                ts = ts,
                apikey = AppConstant.API_KEY,
                hash = AppConfig.getMD5Hash(ts),
                offset = page.toString(),
                limit = state.config.pageSize.toString()
            )
            val responseResult =  response.body()?.data?.results ?: emptyList()
            val isEndOfList = responseResult.isEmpty()
            marvelDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    marvelDatabase.getRemoteKeyDao().clearRemoteKeys()
                    marvelDatabase.getResultDao().clearAllResults()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = responseResult.map {
                    RemoteKeys(resultName = it.name!!, prevKey = prevKey, nextKey = nextKey)
                }
                marvelDatabase.getRemoteKeyDao().insertAll(keys)
                marvelDatabase.getResultDao().insertAll(responseResult)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Results>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
//                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
//                remoteKeys.nextKey
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
//                val remoteKeys = getFirstRemoteKey(state)
////                    ?: throw InvalidObjectException("Invalid state, key should not be null")
////                //end of list condition reached
////                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
////                remoteKeys.prevKey
//                val prevKey = remoteKeys?.prevKey
//                if (prevKey == null) {
//                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                }
//                prevKey
            }
        }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Results>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { results ->
                marvelDatabase.getRemoteKeyDao().remoteKeysResultsId(results.name!!)
            }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, Results>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { results ->
                marvelDatabase.getRemoteKeyDao().remoteKeysResultsId(results.name!!)
            }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, Results>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.name?.let { resultName ->
                marvelDatabase.getRemoteKeyDao().remoteKeysResultsId(resultName)
            }
        }
    }

}