package marvel.api.marvelcharacters.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
class SplashViewModel: ViewModel() {

    var isTimeCompleted: MutableLiveData<String> = MutableLiveData()

    fun initSplashScreen(){
        viewModelScope.launch {
            delay(3000)
            isTimeCompleted.value = String()
        }
    }
}