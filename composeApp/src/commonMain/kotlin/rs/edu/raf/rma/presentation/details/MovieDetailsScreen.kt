package rs.edu.raf.rma.presentation.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import rs.edu.raf.rma.presentation.details.components.CastItem
import rs.edu.raf.rma.presentation.details.components.HeroSection
import rs.edu.raf.rma.presentation.details.components.ImagesGallerySection
import rs.edu.raf.rma.presentation.details.components.InfoSection
import rs.edu.raf.rma.presentation.details.components.OverviewSection
import rs.edu.raf.rma.presentation.details.components.TitleAndInfoSection

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MovieDetailsContract.SideEffect.NavigateBack -> onNavigateBack()
                is MovieDetailsContract.SideEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            val details = state.details

            if (details == null && state.isRefreshing) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (details == null && state.error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: ${state.error!!.message}")
                    Spacer(Modifier.size(8.dp))
                    Button(onClick = { viewModel.setEvent(MovieDetailsContract.UiEvent.Refresh) }) {
                        Text("Retry")
                    }
                }
            } else if (details != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    item {
                        HeroSection(
                            backdropPath = details.backdropUrl ?: details.movie.posterUrl,
                            trailerKey = details.trailerKey,
                            onBackClicked = { viewModel.setEvent(MovieDetailsContract.UiEvent.NavigateBack) },
                            onPlayClicked = {}
                        )
                    }

                    item {
                        TitleAndInfoSection(details = details)
                    }

                    item {
                        OverviewSection(overview = details.overview)
                    }

                    item {
                        InfoSection(details = details)
                    }

                    // 5. Galerija Slika
                    if (details.backdrops.isNotEmpty()) {
                        item {
                            ImagesGallerySection(backdrops = details.backdrops)
                        }
                    }
                    if (details.cast.isNotEmpty()) {
                        item {
                            Text(
                                text = "CAST",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        items(details.cast) { castMember ->
                            CastItem(person = castMember)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}