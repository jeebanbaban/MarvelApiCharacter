package marvel.api.marvelcharacters.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
@Parcelize
data class Thumbnail(
    @SerializedName("path") var path: String? = null,
    @SerializedName("extension") var extension: String? = null
): Parcelable
