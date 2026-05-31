package rs.edu.raf.rma.presentation.watchlist

import rs.edu.raf.rma.domain.model.Movie

interface WatchlistContract {
    data class UiState(
        val movies: List<Movie> = emptyList(),
        val isLoading: Boolean = true,
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data class MovieClicked(val movieId: String) : UiEvent()
        data class RemoveWatchlist(val movie: Movie) : UiEvent()
    }

    sealed class SideEffect {
        data class NavigateToDetails(val movieId: String) : SideEffect()
        data class ShowSnackbar(val message: String) : SideEffect()
    }
}