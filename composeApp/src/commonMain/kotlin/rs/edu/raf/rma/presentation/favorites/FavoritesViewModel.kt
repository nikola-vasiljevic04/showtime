package rs.edu.raf.rma.presentation.favorites

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

class FavoritesViewModel(
    private val repository: CatalogRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<FavoritesContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    private val events = MutableSharedFlow<FavoritesContract.UiEvent>()

    private fun setState(reducer: FavoritesContract.UiState.() -> FavoritesContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    fun setEvent(event: FavoritesContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    init {
        observeEvents()
        observeFavorites()
        syncFavorites()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is FavoritesContract.UiEvent.MovieClicked -> _effects.emit(FavoritesContract.SideEffect.NavigateToDetails(event.movieId))
                    is FavoritesContract.UiEvent.RemoveFavorite -> {
                        runCatching { repository.toggleFavorite(event.movie) }
                            .onFailure { _effects.emit(FavoritesContract.SideEffect.ShowSnackbar("Greška pri uklanjanju.")) }
                    }
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.observeFavorites()
                .catch { e -> setState { copy(error = e, isLoading = false) } }
                .collect { movies -> setState { copy(movies = movies, isLoading = false, error = null) } }
        }
    }

    private fun syncFavorites() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            runCatching { repository.syncFavorites() }
                .onFailure { setState { copy(error = it, isLoading = false) } }
        }
    }
}