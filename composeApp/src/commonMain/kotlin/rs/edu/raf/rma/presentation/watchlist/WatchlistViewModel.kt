package rs.edu.raf.rma.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.domain.repository.CatalogRepository

class WatchlistViewModel(
    private val repository: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WatchlistContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<WatchlistContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    private val events = MutableSharedFlow<WatchlistContract.UiEvent>()

    private fun setState(reducer: WatchlistContract.UiState.() -> WatchlistContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    fun setEvent(event: WatchlistContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    init {
        observeEvents()
        observeWatchlist()
        syncWatchlist()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is WatchlistContract.UiEvent.MovieClicked -> _effects.emit(WatchlistContract.SideEffect.NavigateToDetails(event.movieId))
                    is WatchlistContract.UiEvent.RemoveWatchlist -> {
                        runCatching { repository.toggleWatchlist(event.movie) }
                            .onFailure { _effects.emit(WatchlistContract.SideEffect.ShowSnackbar("Greška pri uklanjanju.")) }
                    }
                }
            }
        }
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            repository.observeWatchlist()
                .catch { e -> setState { copy(error = e, isLoading = false) } }
                .collect { movies -> setState { copy(movies = movies, isLoading = false, error = null) } }
        }
    }

    private fun syncWatchlist() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            runCatching { repository.syncWatchlist() }
                .onFailure { setState { copy(error = it, isLoading = false) } }
        }
    }
}