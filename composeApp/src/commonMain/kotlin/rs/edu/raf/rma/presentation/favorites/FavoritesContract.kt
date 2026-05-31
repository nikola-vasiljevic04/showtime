package rs.edu.raf.rma.presentation.favorites

import rs.edu.raf.rma.domain.model.Movie

interface FavoritesContract {
    data class UiState(
        val movies: List<Movie> = emptyList(),
        val isLoading: Boolean = true,
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data class MovieClicked(val movieId: String) : UiEvent()
        data class RemoveFavorite(val movie: Movie) : UiEvent()
    }

    sealed class SideEffect {
        data class NavigateToDetails(val movieId: String) : SideEffect()
        data class ShowSnackbar(val message: String) : SideEffect()
    }
}