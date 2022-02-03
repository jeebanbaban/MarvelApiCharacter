package marvel.api.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
data class Data(
    @SerializedName("offset") var offset: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("count") var count: Int? = null,
    @SerializedName("results") var results: List<Results> = listOf()
)
