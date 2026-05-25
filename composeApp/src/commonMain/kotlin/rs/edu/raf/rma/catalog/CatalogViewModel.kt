package rs.edu.raf.rma.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(private val repository: CatalogRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogContract.UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.moviesStream.collect { movieList ->
                _uiState.update { it.copy(movies = movieList) }
            }
        }
        onEvent(CatalogContract.UiEvent.RefreshMovies)
    }
    fun onEvent(event: CatalogContract.UiEvent) {
        when (event) {
            is CatalogContract.UiEvent.RefreshMovies -> fetchMovies()
        }
    }

    private fun fetchMovies() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.fetchAndSaveMovies()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Greška pri preuzimanju filmova") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}