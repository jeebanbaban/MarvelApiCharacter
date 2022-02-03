package marvel.api.marvelcharacters.data.source.remote

import marvel.api.marvelcharacters.data.source.remote.Services.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
object ApiClient {

    private var apiService: ApiService? = null

    fun getService(): ApiService {
        return apiService ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java).also {
                apiService = it
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
//            .addInterceptor(SupportInterceptor())
//            .authenticator(SupportInterceptor())
        return builder.build()
    }
}