package marvel.api.marvelcharacters.data.source.remote

import marvel.api.marvelcharacters.data.model.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
interface ApiService {

    @GET(Services.MARVEL_CHARACTERS)
    suspend fun getMarvelCharacters(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String
    ): Response<CharacterResponse>

    @GET(Services.MARVEL_SINGLE_CHARACTER)
    suspend fun getMarvelSingleCharacter(
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Path("character_id") character_id: String
    ): Response<CharacterResponse>
}