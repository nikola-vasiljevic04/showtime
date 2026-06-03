package rs.edu.raf.rma.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.auth.AuthRepository
import rs.edu.raf.rma.domain.repository.CatalogRepository
import rs.edu.raf.rma.domain.repository.ProfileRepository

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val catalogRepository: CatalogRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ProfileContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    private val events = MutableSharedFlow<ProfileContract.UiEvent>()

    private fun setState(reducer: ProfileContract.UiState.() -> ProfileContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    fun setEvent(event: ProfileContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    init {
        observeEvents()
        observeListCounts()
        setEvent(ProfileContract.UiEvent.LoadProfile)
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is ProfileContract.UiEvent.LoadProfile -> loadProfile()
                    is ProfileContract.UiEvent.LogoutClicked -> performLogout()
                }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            runCatching { profileRepository.getProfile() }
                .onSuccess { user ->
                    setState {
                        copy(
                            isLoading = false,
                            fullName = user.fullName ?: "",
                            username = user.username
                        )
                    }
                }
                .onFailure { e ->
                    setState { copy(isLoading = false, error = e) }
                    _effects.emit(ProfileContract.SideEffect.ShowSnackbar("Error loadning"))
                }
        }
    }

    private fun observeListCounts() {
        viewModelScope.launch {
            catalogRepository.observeFavorites()
                .catch {  }
                .collect { list -> setState { copy(favoritesCount = list.size) } }
        }

        viewModelScope.launch {
            catalogRepository.observeWatchlist()
                .catch {  }
                .collect { list -> setState { copy(watchlistCount = list.size) } }
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}