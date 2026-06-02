package rs.edu.raf.rma.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import rs.edu.raf.rma.domain.model.Movie
import rs.edu.raf.rma.domain.repository.CatalogRepository
import rs.edu.raf.rma.presentation.state.FilterManager

class CatalogViewModel(
    private val repository: CatalogRepository,
    private val filterManager: FilterManager
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<CatalogContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    private val events = MutableSharedFlow<CatalogContract.UiEvent>()

    private fun setState(reducer: CatalogContract.UiState.() -> CatalogContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    fun setEvent(event: CatalogContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    init {
        observeEvents()
        observeMovies()
        syncMovies()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is CatalogContract.UiEvent.ChangeSort -> setState { copy(currentSort = event.sortBy) }
                    is CatalogContract.UiEvent.MovieClicked -> _effects.emit(CatalogContract.SideEffect.NavigateToDetails(event.movieId))
                    is CatalogContract.UiEvent.FilterClicked -> _effects.emit(CatalogContract.SideEffect.NavigateToFilters)
                    is CatalogContract.UiEvent.ToggleFavorite -> toggleFavorite(event.movie)
                    is CatalogContract.UiEvent.ToggleWatchlist -> toggleWatchlist(event.movie)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeMovies() {
        viewModelScope.launch {
            combine(
                filterManager.activeFilters,
                _state.map { it.currentSort }.distinctUntilChanged()
            ) { filters, sort -> Pair(filters, sort) }
                .flatMapLatest { (filters, sort) ->
                    repository.observeMovies(filters, sort)
                }
                .catch { e -> setState { copy(error = e, isLoading = false) } }
                .collect { movies ->
                    setState { copy(movies = movies, isLoading = false, error = null) }
                }
        }
    }

    private fun syncMovies() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            val moviesDeferred = async { runCatching { repository.syncMoviesIfNeeded() } }
            val favoritesDeferred = async { runCatching { repository.syncFavorites() } }
            val watchlistDeferred = async { runCatching { repository.syncWatchlist() } }

            val moviesResult = moviesDeferred.await()
            val favoritesResult = favoritesDeferred.await()
            val watchlistResult = watchlistDeferred.await()
            moviesResult.onFailure { e ->
                setState { copy(error = e) }
            }
            favoritesResult.onFailure {
                _effects.emit(CatalogContract.SideEffect.ShowSnackbar("Error loading favorite movies"));
            }
            watchlistResult.onFailure {
                _effects.emit(CatalogContract.SideEffect.ShowSnackbar("Error loading watchlist"));
            }

            setState { copy(isLoading = false) }
        }
    }


    private fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            runCatching { repository.toggleFavorite(movie) }
                .onFailure { _effects.emit(CatalogContract.SideEffect.ShowSnackbar("Error updating watchlist. Please try again later.")) }
        }
    }

    private fun toggleWatchlist(movie: Movie) {
        viewModelScope.launch {
            runCatching { repository.toggleWatchlist(movie) }
                .onFailure { _effects.emit(CatalogContract.SideEffect.ShowSnackbar("Error updating favorites. Please try again later.")) }
        }
    }
}