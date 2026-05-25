package rs.edu.raf.rma.catalog

import rs.edu.raf.rma.core.db.MovieEntity

interface CatalogContract {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val movies: List<MovieEntity> = emptyList()
    )
    sealed class UiEvent{
        data object RefreshMovies : UiEvent()
    }
}