package rs.edu.raf.rma.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rs.edu.raf.rma.core.auth.AuthStore
import rs.edu.raf.rma.core.auth.model.AuthState
import kotlin.time.Duration.Companion.seconds

class SplashViewModel(
    private val authStore: AuthStore
) : ViewModel() {

    private val _bootState = MutableStateFlow<BootState>(BootState.Loading)
    val bootState: StateFlow<BootState> = _bootState.asStateFlow()
    val isLoggedIn: StateFlow<Boolean> = authStore.authState
        .map { it is AuthState.Authenticated }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    init {
        checkAuthState()
    }

    private fun checkAuthState() = viewModelScope.launch {
        try {
            authStore.awaitInitialAuthState()
            delay(2.seconds)
            _bootState.value = BootState.Success
        } catch (e: Exception) {
            _bootState.value = BootState.Failed(e)
        }
    }

    fun retryBoot() {
        _bootState.value = BootState.Loading
        checkAuthState()
    }
}