package rs.edu.raf.rma.presentation.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rs.edu.raf.rma.presentation.catalog.CatalogContract
import rs.edu.raf.rma.domain.model.Movie

@Composable
fun CatalogContent(
    state: CatalogContract.UiState,
    onSortChanged: (String) -> Unit,
    onMovieClick: (Movie) -> Unit,
    onFavoriteClick: (Movie) -> Unit,
    onWatchlistClick: (Movie) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(
        state.movies.firstOrNull()?.id,
        state.movies.size
    ) {
        listState.scrollToItem(0)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SortBar(
            currentSort = state.currentSort,
            totalMovies = state.movies.size,
            onSortChanged = onSortChanged
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = state.movies,
                key = { it.id }
            ) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie) },
                    onFavoriteClick = { onFavoriteClick(movie) },
                    onWatchlistClick = { onWatchlistClick(movie) }
                )
            }
        }
    }
}