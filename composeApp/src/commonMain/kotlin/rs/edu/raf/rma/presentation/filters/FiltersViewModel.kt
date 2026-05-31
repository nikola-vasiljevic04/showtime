package rs.edu.raf.rma.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.domain.repository.CatalogRepository
import rs.edu.raf.rma.presentation.state.FilterData
import rs.edu.raf.rma.presentation.state.FilterManager

class FiltersViewModel(
    private val repository: CatalogRepository,
    private val filterManager: FilterManager
) : ViewModel() {

    private val _state = MutableStateFlow(FiltersContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<FiltersContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    private val events = MutableSharedFlow<FiltersContract.UiEvent>()

    private fun setState(reducer: FiltersContract.UiState.() -> FiltersContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    fun setEvent(event: FiltersContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    init {
        observeEvents()
        loadSavedFilters()
        setEvent(FiltersContract.UiEvent.LoadGenres)
    }

    private fun loadSavedFilters() {
        val savedFilters = filterManager.activeFilters.value
        setState {
            copy(
                searchQuery = savedFilters.query,
                selectedGenreId = savedFilters.genreId,
                minRating = savedFilters.minRating,
                minYear = savedFilters.minYear,
                maxYear = savedFilters.maxYear
            )
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is FiltersContract.UiEvent.LoadGenres -> fetchGenres()
                    is FiltersContract.UiEvent.ChangeSearchQuery -> setState { copy(searchQuery = event.query) }
                    is FiltersContract.UiEvent.SelectGenre -> {
                        val newGenreId = if (state.value.selectedGenreId == event.genreId) null else event.genreId
                        setState { copy(selectedGenreId = newGenreId) }
                    }
                    is FiltersContract.UiEvent.SetMinRating -> setState { copy(minRating = event.rating) }
                    is FiltersContract.UiEvent.SetMinYear -> setState { copy(minYear = event.year) }
                    is FiltersContract.UiEvent.SetMaxYear -> setState { copy(maxYear = event.year) }
                    is FiltersContract.UiEvent.ClearFilters -> {
                        filterManager.clearFilters()
                        setState {
                            copy(
                                searchQuery = "",
                                selectedGenreId = null,
                                minYear = null,
                                maxYear = null,
                                minRating = 0f
                            )
                        }
                    }
                    is FiltersContract.UiEvent.ApplyFilters -> {
                        val currentState = state.value
                        filterManager.updateFilters(
                            FilterData(
                                query = currentState.searchQuery,
                                genreId = currentState.selectedGenreId,
                                minRating = currentState.minRating,
                                minYear = currentState.minYear,
                                maxYear = currentState.maxYear
                            )
                        )
                        viewModelScope.launch {
                            _effects.emit(FiltersContract.SideEffect.NavigateBack)
                        }
                    }
                }
            }
        }
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            setState { copy(genreState = FiltersContract.GenreState.Loading) }
            try {
                val genres = repository.getGenres()
                if (genres.isEmpty()) {
                    setState { copy(genreState = FiltersContract.GenreState.Error("Nema žanrova u bazi")) }
                } else {
                    setState { copy(genreState = FiltersContract.GenreState.Success(genres)) }
                }
            } catch (e: Exception) {
                setState { copy(genreState = FiltersContract.GenreState.Error(e.message ?: "Greška pri učitavanju žanrova")) }
            }
        }
    }
}