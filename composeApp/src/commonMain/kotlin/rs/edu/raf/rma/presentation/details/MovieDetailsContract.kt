package rs.edu.raf.rma.presentation.details

import rs.edu.raf.rma.domain.model.MovieDetails

interface MovieDetailsContract {
    data class UiState(
        val details: MovieDetails? = null,
        val isRefreshing: Boolean = true,
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data object Refresh : UiEvent()
        data class PlayTrailer(val videoKey: String) : UiEvent() // Dodato
        data object ToggleFavorite : UiEvent()
        data object ToggleWatchlist : UiEvent()
        data object NavigateBack : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateBack : SideEffect()
        data class OpenYoutube(val videoKey: String) : SideEffect() // Dodato
        data class ShowSnackbar(val message: String) : SideEffect()
    }
}