package rs.edu.raf.rma.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class ErrorResponse(val message: String? = null)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthContract.UiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthContract.SideEffect>()
    val effect = _effect.asSharedFlow()

    private val jsonParser = Json { ignoreUnknownKeys = true }
    private val usernameRegex = "^[a-zA-Z0-9_]{3,}\$".toRegex()

    fun onEvent(event: AuthContract.UiEvent) {
        when (event) {
            is AuthContract.UiEvent.UpdateUsername -> _uiState.update { it.copy(username = event.text, error = null) }
            is AuthContract.UiEvent.UpdatePassword -> _uiState.update { it.copy(password = event.text, error = null) }
            is AuthContract.UiEvent.UpdateFullName -> _uiState.update { it.copy(fullName = event.text, error = null) }

            is AuthContract.UiEvent.Login -> performLogin()
            is AuthContract.UiEvent.Signup -> performSignup()

            is AuthContract.UiEvent.NavigateTo -> _uiState.update {
                it.copy(currentScreen = event.screen, error = null, username = "", password = "", fullName = "")
            }
            is AuthContract.UiEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun performLogin() {
        val state = _uiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Unesite username i password.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.login(state.username, state.password)
                _effect.emit(AuthContract.SideEffect.NavigateToCatalog)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = extractErrorMessage(e)) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun performSignup() {
        val state = _uiState.value

        // Specifikacija: Validacija pre slanja
        if (state.fullName.isBlank()) {
            _uiState.update { it.copy(error = "Ime i prezime su obavezni.") }
            return
        }
        if (!state.username.matches(usernameRegex)) {
            _uiState.update { it.copy(error = "Username mora imati min 3 karaktera (samo slova, cifre i _).") }
            return
        }
        if (state.password.length < 8) {
            _uiState.update { it.copy(error = "Password mora imati najmanje 8 karaktera.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.signup(state.fullName, state.username, state.password)
                _effect.emit(AuthContract.SideEffect.NavigateToCatalog)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = extractErrorMessage(e)) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun extractErrorMessage(e: Exception): String {
        return try {
            if (e is ResponseException) {
                val errorString = e.response.bodyAsText()
                val parsed = jsonParser.decodeFromString<ErrorResponse>(errorString)
                parsed.message ?: "Nevalidni kredencijali ili zauzet username."
            } else {
                "Proverite internet konekciju."
            }
        } catch (parseEx: Exception) {
            "Mrežna greška."
        }
    }
}