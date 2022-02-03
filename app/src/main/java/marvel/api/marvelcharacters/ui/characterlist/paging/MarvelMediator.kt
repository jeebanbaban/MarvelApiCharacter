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
import java.io.InvalidObjectException
import java.sql.Timestamp

/**
 * Created by Jeeban Bagdi on 2/1/2022.
 */
@ExperimentalPagingApi
class MarvelMediator(val apiService: ApiService, val marvelDatabase: MarvelDatabase) :
    RemoteMediator<Int, Results>() {

    private val INITIALL_PAGE: Int = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Results>
    ): MediatorResult {

        try {
            // Judging the page number
            println("LLLLLLLLLLLLLLLLL 11111111111111111111111"+loadType.toString())
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR")
                    val remoteKey = getClosestRemoteKeys(state)
                    remoteKey?.nextKey?.minus(1) ?: INITIALL_PAGE
                }
                LoadType.PREPEND -> {
                    println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=="+getLastRemoteKey(state).toString())
                    val remoteKey = getLastRemoteKey(state)
//                            ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                    remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
//                    remoteKey?.nextKey ?: 2

                }
            }

            println("LLLLLLLLLLLL 2222222222222222222222222"+loadType.toString())

            // make network request
            val ts = Timestamp(System.currentTimeMillis()).time.toString()
            val response = ApiClient.getService().getMarvelCharacters(
                ts = ts,
                apikey = AppConstant.API_KEY,
                hash = AppConfig.getMD5Hash(ts),
                offset = page.toString(),
                limit = state.config.pageSize.toString()
            )
            val responseResults = response.body()?.data?.results ?: emptyList()
//            val endOfPagination = responseResults.size < state.config.pageSize
            val endOfPagination = responseResults.isEmpty()

            if (response.isSuccessful) {
                if (!responseResults.isNullOrEmpty()) {
                    // flush our data
                    if (loadType == LoadType.REFRESH) {
                        marvelDatabase.getResultDao().clearAllResults()
                        marvelDatabase.getRemoteKeyDao().clearRemoteKeys()
                    }
                    val prevKey = if (page == INITIALL_PAGE) null else page - 1
                    val nextKey = if (endOfPagination) null else page + 1
                    val remoteKeyList = responseResults.map {
                        RemoteKeys(resultName = it.name!!, prevKey = prevKey, nextKey = nextKey)
                    }

                    // make list of remote keys
                    marvelDatabase.getRemoteKeyDao().insertAll(remoteKeyList)
                    // insert to the room
                    marvelDatabase.getResultDao().insertAll(responseResults)
                    println("kkkkkkkkkkkkk=="+marvelDatabase.getRemoteKeyDao().getRemoteKeys())
                    println("rrrrrrrrrrrrr=="+marvelDatabase.getResultDao().getResults().size)
                }
                println("eeeeeeeeeeeeeeeeeeee 111111111111111111==="+endOfPagination)
               return MediatorResult.Success(endOfPagination)
            } else {
                println("eeeeeeeeeeeeeeeeeeee 22222222222222222==="+endOfPagination)
              return  MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (exception: Exception) {
           return MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeys(): RemoteKeys? {
        return marvelDatabase.getRemoteKeyDao().getRemoteKeys().firstOrNull()
    }

    private suspend fun getClosestRemoteKeys(state: PagingState<Int, Results>): RemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                marvelDatabase.getRemoteKeyDao().remoteKeysResultsId(it.name!!)
            }
        }

    }

//    private suspend fun getLastRemoteKey(state: PagingState<Int, Results>): RemoteKeys? {
//        return state.lastItemOrNull()?.let {
//            marvelDatabase.getRemoteKeyDao().remoteKeysResultsId(it.name!!)
//        }
//    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Results>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { results ->
                marvelDatabase.getRemoteKeyDao().remoteKeysResultsId(results.name!!)
            }
    }

}