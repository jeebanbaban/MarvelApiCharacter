package marvel.api.marvelcharacters.utils

import marvel.api.marvelcharacters.utils.AppConstant.Companion.API_KEY
import marvel.api.marvelcharacters.utils.AppConstant.Companion.PRIVATE_KEY
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

/**
 * Created by Jeeban Bagdi on 1/31/2022.
 */
class AppConfig {
    companion object{
//        val ts = Timestamp(System.currentTimeMillis()).time.toString()

        fun getMD5Hash(ts: String): String{
            val input = "$ts${PRIVATE_KEY}${API_KEY}"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }
}