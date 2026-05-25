package rs.edu.raf.rma.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val jsonParser = Json { ignoreUnknownKeys = true }
    fun onEvent(event: AuthContract.UiEvent) {
        when (event) {
            // Unos podataka
            is AuthContract.UiEvent.UpdateUsername -> _uiState.update { it.copy(username = event.text) }
            is AuthContract.UiEvent.UpdatePassword -> _uiState.update { it.copy(password = event.text) }
            is AuthContract.UiEvent.UpdateFullName -> _uiState.update { it.copy(fullName = event.text) }

            // Login i Signup koriste podatke direktno iz stanja (state.username, state.password)
            is AuthContract.UiEvent.Login -> performLogin()
            is AuthContract.UiEvent.Signup -> performSignup()

            is AuthContract.UiEvent.NavigateTo -> _uiState.update { it.copy(currentScreen = event.screen, error = null) }
            is AuthContract.UiEvent.ClearError -> _uiState.update { it.copy(error = null) }
        }
    }

    private fun performLogin() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.login(state.username, state.password)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = extractErrorMessage(e)) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun performSignup() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.signup(state.fullName, state.username, state.password)
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
                parsed.message ?: "Došlo je do greške na serveru."
            } else {
                "Proverite internet konekciju."
            }
        } catch (parseEx: Exception) {
            "Mrežna greška."
        }
    }
}