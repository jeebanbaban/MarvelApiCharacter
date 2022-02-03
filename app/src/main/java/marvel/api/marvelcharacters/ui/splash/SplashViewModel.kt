package marvel.api.marvelcharacters.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
class SplashViewModel: ViewModel() {

    fun isSplashTimeCompleted() = flow {
        delay(3000)
        emit(true)
    }
}