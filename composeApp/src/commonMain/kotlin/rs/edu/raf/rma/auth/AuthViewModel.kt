package rs.edu.raf.rma.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthContract.UiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: AuthContract.UiEvent) {
        when (event) {
            is AuthContract.UiEvent.Login -> performLogin(event.user, event.pass)
            is AuthContract.UiEvent.Signup -> performSignup(event.name, event.user, event.pass)
            is AuthContract.UiEvent.NavigateTo -> _uiState.value = _uiState.value.copy(currentScreen = event.screen)
            is AuthContract.UiEvent.ClearError -> _uiState.value = _uiState.value.copy(error = null)
        }
    }

    private fun performLogin(user: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.login(user, pass)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun performSignup(name: String, user: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.signup(name, user, pass)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}