package rs.edu.raf.rma.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rs.edu.raf.rma.presentation.profile.components.LogoutButton
import rs.edu.raf.rma.presentation.profile.components.StatCard
import rs.edu.raf.rma.presentation.profile.components.UserProfileHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ProfileContract.SideEffect.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
                is ProfileContract.SideEffect.NavigateToAuth -> { /* tvoja navigacija */ }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Profile", fontWeight = FontWeight.Bold) }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading && state.fullName.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    UserProfileHeader(
                        fullName = state.fullName,
                        username = state.username
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Favorites",
                            count = state.favoritesCount,
                            icon = Icons.Filled.Favorite,
                            tint = Color.Red
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Watchlist",
                            count = state.watchlistCount,
                            icon = Icons.Filled.Bookmark,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Best Score",
                            count = state.bestScore.toInt(),
                            icon = Icons.Filled.Star,
                            tint = Color(0xFFFFC107)
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Quizzes Played",
                            count = state.gamesPlayed,
                            icon = Icons.Filled.Quiz,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    LogoutButton(
                        onClick = { viewModel.setEvent(ProfileContract.UiEvent.LogoutClicked) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}