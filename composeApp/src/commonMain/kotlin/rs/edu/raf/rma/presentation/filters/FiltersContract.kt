package rs.edu.raf.rma.presentation.filters

import rs.edu.raf.rma.domain.model.Genre

interface FiltersContract {

    data class UiState(
        val searchQuery: String = "",
        val genreState: GenreState = GenreState.Loading,
        val selectedGenreId: Int? = null,
        val minYear: Int? = null,
        val maxYear: Int? = null,
        val minRating: Float = 0f
    )

    sealed interface GenreState {
        data object Loading : GenreState
        data class Success(val genres: List<Genre>) : GenreState
        data class Error(val message: String) : GenreState
    }

    sealed class UiEvent {
        data object LoadGenres : UiEvent()
        data class ChangeSearchQuery(val query: String) : UiEvent()
        data class SelectGenre(val genreId: Int?) : UiEvent()
        data class SetMinRating(val rating: Float) : UiEvent()
        data class SetMinYear(val year: Int?) : UiEvent()
        data class SetMaxYear(val year: Int?) : UiEvent()
        data object ApplyFilters : UiEvent()
        data object ClearFilters : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateBack : SideEffect()
    }
}