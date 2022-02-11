package marvel.api.marvelcharacters.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import marvel.api.marvelcharacters.data.model.RemoteKeys

/**
 * Created by Jeeban Bagdi on 2/1/2022.
 */
@Dao
interface RemoteKeysDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Insert(onConflict = REPLACE)
    suspend fun saveRemoteKeys(remoteKeys: RemoteKeys)

    @Query("SELECT * FROM remotekeys ORDER BY resultName DESC")
    suspend fun getRemoteKeys(): List<RemoteKeys>

    @Query("SELECT * FROM remotekeys WHERE resultName = :resultName")
    suspend fun remoteKeysResultsId(resultName: String): RemoteKeys?

    @Query("DELETE FROM remotekeys")
    suspend fun clearRemoteKeys()
}