package marvel.api.marvelcharacters.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
@Parcelize
data class Series(
    @SerializedName("available") var available: Int? = null,
    @SerializedName("collectionURI") var collectionURI: String? = null,
    @SerializedName("items") var itemsSeries: ArrayList<Items> = arrayListOf(),
    @SerializedName("returned") var returned: Int? = null
): Parcelable
