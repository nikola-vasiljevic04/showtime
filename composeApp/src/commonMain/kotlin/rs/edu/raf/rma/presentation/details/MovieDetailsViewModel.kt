package rs.edu.raf.rma.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.domain.repository.CatalogRepository

class MovieDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CatalogRepository,
) : ViewModel() {

    private val argMovieId: String = checkNotNull(savedStateHandle["movieId"]) { "Nedostaje movieId" }

    private val _state = MutableStateFlow(MovieDetailsContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<MovieDetailsContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    private val events = MutableSharedFlow<MovieDetailsContract.UiEvent>()

    private fun setState(reducer: MovieDetailsContract.UiState.() -> MovieDetailsContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    fun setEvent(event: MovieDetailsContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    init {
        observeEvents()
        observeMovieDetails()
        refresh()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is MovieDetailsContract.UiEvent.Refresh -> refresh()
                    is MovieDetailsContract.UiEvent.ToggleFavorite -> toggleFavorite()
                    is MovieDetailsContract.UiEvent.ToggleWatchlist -> toggleWatchlist()
                    is MovieDetailsContract.UiEvent.NavigateBack -> _effects.emit(MovieDetailsContract.SideEffect.NavigateBack)
                    is MovieDetailsContract.UiEvent.PlayTrailer -> _effects.emit(MovieDetailsContract.SideEffect.OpenYoutube(event.videoKey))
                }
            }
        }
    }

    private fun observeMovieDetails() {
        viewModelScope.launch {
            repository.observeMovieDetails(argMovieId).collect { details ->
                setState { copy(details = details) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isRefreshing = true, error = null) }
            runCatching { repository.refreshMovieDetails(argMovieId) }
                .onFailure { setState { copy(error = it) } }
            setState { copy(isRefreshing = false) }
        }
    }

    private fun toggleFavorite() {
        val currentMovie = state.value.details?.movie ?: return
        viewModelScope.launch {
            runCatching { repository.toggleFavorite(currentMovie) }
                .onFailure { _effects.emit(MovieDetailsContract.SideEffect.ShowSnackbar("Greška pri ažuriranju omiljenih.")) }
        }
    }

    private fun toggleWatchlist() {
        val currentMovie = state.value.details?.movie ?: return
        viewModelScope.launch {
            runCatching { repository.toggleWatchlist(currentMovie) }
                .onFailure { _effects.emit(MovieDetailsContract.SideEffect.ShowSnackbar("Greška pri ažuriranju watchlist-e.")) }
        }
    }
}