package rs.edu.raf.rma.presentation.catalog

import rs.edu.raf.rma.domain.model.Movie

interface CatalogContract {
    data class UiState(
        val movies: List<Movie> = emptyList(),
        val currentSort: String = "imdb_rating",
        val isLoading: Boolean = true,
        val error: Throwable? = null,
    ) {
        val isEmpty: Boolean get() = !isLoading && movies.isEmpty() && error == null
    }

    sealed class UiEvent {
        data class ChangeSort(val sortBy: String) : UiEvent()
        data class MovieClicked(val movieId: String) : UiEvent()
        data object FilterClicked : UiEvent()
        data class ToggleFavorite(val movie: Movie) : UiEvent()
        data class ToggleWatchlist(val movie: Movie) : UiEvent()
    }

    sealed class SideEffect {
        data class NavigateToDetails(val movieId: String) : SideEffect()
        data object NavigateToFilters : SideEffect()
        data class ShowSnackbar(val message: String) : SideEffect()
    }
}