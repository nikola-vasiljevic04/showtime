package rs.edu.raf.rma.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import rs.edu.raf.rma.presentation.profile.components.LogoutButton
import rs.edu.raf.rma.presentation.profile.components.UserProfileHeader
import rs.edu.raf.rma.presentation.profile.components.UserStatsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ProfileContract.SideEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                else -> {}
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

                    UserStatsSection(
                        favoritesCount = state.favoritesCount,
                        watchlistCount = state.watchlistCount
                    )

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