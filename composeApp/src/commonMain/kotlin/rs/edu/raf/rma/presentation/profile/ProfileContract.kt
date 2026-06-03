package rs.edu.raf.rma.presentation.profile

interface ProfileContract {
    data class UiState(
        val fullName: String = "",
        val username: String = "",
        val favoritesCount: Int = 0,
        val watchlistCount: Int = 0,
        val bestScore: Float = 0f,
        val gamesPlayed: Int = 0,
        val isLoading: Boolean = true,
        val error: Throwable? = null
    )

    sealed class UiEvent {
        data object LoadProfile : UiEvent()
        data object LogoutClicked : UiEvent()
    }

    sealed class SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect()
        data object NavigateToAuth : SideEffect()
    }
}