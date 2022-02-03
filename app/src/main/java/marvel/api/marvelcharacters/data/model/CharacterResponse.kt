package marvel.api.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
data class CharacterResponse(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("copyright") var copyright: String? = null,
    @SerializedName("attributionText") var attributionText: String? = null,
    @SerializedName("attributionHTML") var attributionHTML: String? = null,
    @SerializedName("etag") var etag: String? = null,
    @SerializedName("data") var data: Data? = Data()
)
