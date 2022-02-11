package marvel.api.marvelcharacters.data.source.remote

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
object Services {

    //Base Url
    const val BASE_URL = "https://gateway.marvel.com/"

    const val MARVEL_CHARACTERS = "v1/public/characters"
//    const val MARVEL_SINGLE_CHARACTER = "/v1/public/characters/1009144"
    const val MARVEL_SINGLE_CHARACTER = "/v1/public/characters/{character_id}"
}