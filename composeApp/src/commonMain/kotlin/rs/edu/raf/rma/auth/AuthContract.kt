package rs.edu.raf.rma.auth
interface AuthContract {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val currentScreen: AuthScreen = AuthScreen.LANDING
    )

    enum class AuthScreen {
        LANDING, LOGIN, SIGNUP
    }

    sealed class UiEvent {
        data class NavigateTo(val screen: AuthScreen) : UiEvent()
        data class Login(val user: String, val pass: String) : UiEvent()
        data class Signup(val name: String, val user: String, val pass: String) : UiEvent()
        data object ClearError : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateToCatalog : SideEffect()
    }
}