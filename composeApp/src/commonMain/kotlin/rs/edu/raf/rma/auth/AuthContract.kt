package rs.edu.raf.rma.auth
interface AuthContract {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val currentScreen: AuthScreen = AuthScreen.LOGIN,
        val username: String = "",
        val password: String = "",
        val fullName: String = ""
    )

    enum class AuthScreen {
        LANDING, LOGIN, SIGNUP
    }

    sealed class UiEvent {
        data class NavigateTo(val screen: AuthScreen) : UiEvent()
        data class Login(val user: String, val pass: String) : UiEvent()
        data class Signup(val name: String, val user: String, val pass: String) : UiEvent()
        data object ClearError : UiEvent()
        data class UpdateUsername(val text: String) : UiEvent()
        data class UpdatePassword(val text: String) : UiEvent()
        data class UpdateFullName(val text: String) : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateToCatalog : SideEffect()
    }
}