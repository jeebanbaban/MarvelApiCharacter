package marvel.api.marvelcharacters.application

import android.app.Application

class MyApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null
        fun getInstance(): MyApplication{
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}