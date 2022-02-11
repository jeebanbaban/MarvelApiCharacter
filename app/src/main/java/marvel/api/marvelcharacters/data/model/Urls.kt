package marvel.api.marvelcharacters.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
@Parcelize
data class Urls(
    @SerializedName("type") var type: String? = null,
    @SerializedName("url") var url: String? = null
): Parcelable
