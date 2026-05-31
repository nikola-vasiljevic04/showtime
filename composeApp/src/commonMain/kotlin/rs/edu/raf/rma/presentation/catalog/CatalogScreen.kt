package rs.edu.raf.rma.presentation.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import rs.edu.raf.rma.presentation.catalog.components.CatalogContent
import rs.edu.raf.rma.presentation.catalog.components.CatalogTopBar
import rs.edu.raf.rma.presentation.catalog.components.EmptyState
import rs.edu.raf.rma.presentation.catalog.components.ErrorState
import rs.edu.raf.rma.presentation.catalog.components.LoadingState

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onThemeToggle: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToFilters: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CatalogContract.SideEffect.NavigateToDetails -> {
                    onNavigateToDetails(effect.movieId)
                }
                is CatalogContract.SideEffect.NavigateToFilters -> {
                    onNavigateToFilters()
                }
                is CatalogContract.SideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CatalogTopBar(
                onThemeToggle = onThemeToggle,
                onFilterClick = {
                    viewModel.setEvent(CatalogContract.UiEvent.FilterClicked)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                LoadingState()
            } else if (state.error != null) {
                ErrorState(state.error?.message ?: "Došlo je do greške")
            } else if (state.isEmpty) {
                EmptyState()
            } else {
                CatalogContent(
                    state = state,
                    onSortChanged = {
                        viewModel.setEvent(CatalogContract.UiEvent.ChangeSort(it))
                    },
                    onMovieClick = {
                        viewModel.setEvent(CatalogContract.UiEvent.MovieClicked(it.id))
                    },
                    onFavoriteClick = {
                        viewModel.setEvent(CatalogContract.UiEvent.ToggleFavorite(it))
                    },
                    onWatchlistClick = {
                        viewModel.setEvent(CatalogContract.UiEvent.ToggleWatchlist(it))
                    }
                )
            }
        }
    }
}