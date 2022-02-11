package marvel.api.marvelcharacters.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import marvel.api.marvelcharacters.data.model.Results

/**
 * Created by Jeeban Bagdi on 2/1/2022.
 */
@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<Results>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(results: Results)

    @Query("SELECT * FROM results")
    fun getAllResults(): PagingSource<Int, Results>

    @Query("SELECT * FROM results")
    fun getResults(): List<Results>

    @Query("DELETE FROM results")
    suspend fun clearAllResults()
}