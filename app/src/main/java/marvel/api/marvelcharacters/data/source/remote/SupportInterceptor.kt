package marvel.api.marvelcharacters.data.source.remote

import okhttp3.*

/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
class SupportInterceptor: Interceptor, Authenticator {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().addHeader("","").build()
        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        var requestAvailable: Request? = null
        try {
            requestAvailable = response.request.newBuilder().addHeader("","").build()
            return  requestAvailable
        }catch (e: Exception){}
        return requestAvailable
    }
}