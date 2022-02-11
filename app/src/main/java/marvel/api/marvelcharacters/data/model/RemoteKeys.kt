package marvel.api.marvelcharacters.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Jeeban Bagdi on 2/1/2022.
 */
@Entity
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val resultName: String,
    val prevKey: Int?,
    val nextKey: Int?
)
