package rs.edu.raf.rma.presentation.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FilterData(
    val query: String = "",
    val genreId: Int? = null,
    val minRating: Float = 0f,
    val minYear: Int? = null,
    val maxYear: Int? = null
)

class FilterManager {
    private val _activeFilters = MutableStateFlow(FilterData())
    val activeFilters: StateFlow<FilterData> = _activeFilters.asStateFlow()

    fun updateFilters(newFilters: FilterData) {
        _activeFilters.value = newFilters
    }

    fun clearFilters() {
        _activeFilters.value = FilterData()
    }
}