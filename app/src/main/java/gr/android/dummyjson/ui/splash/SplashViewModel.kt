package gr.android.dummyjson.ui.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.android.dummyjson.R
import gr.android.dummyjson.domain.usecases.SilentLoginUseCase
import gr.android.dummyjson.ui.BaseViewModelImpl
import gr.android.dummyjson.ui.emitAsync
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val silentLoginUseCase: SilentLoginUseCase
) : BaseViewModelImpl<SplashContract.State, SplashContract.Event>() {

    private val _uiState = MutableStateFlow<SplashContract.State>(
        SplashContract.State.Data(splashDrawable = R.drawable.ic_splash_logo)
    )

    override val uiState: StateFlow<SplashContract.State?>
        get() = _uiState

    init {
        viewModelScope.launch {
            delay(3000)
            silentLoginUseCase.isLoggedIn().collect{
                if (it) {
                    events.emitAsync(SplashContract.Event.NavigateToHomeScreen)
                } else {
                    events.emitAsync(SplashContract.Event.NavigateToLoginScreen)
                }
            }
        }
    }

}