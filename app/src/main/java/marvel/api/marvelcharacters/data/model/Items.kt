package marvel.api.marvelcharacters.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
@Parcelize
data class Items(
    @SerializedName("resourceURI") var resourceURI: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("type") var type: String? = null
): Parcelable
