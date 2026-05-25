package rs.edu.raf.rma

import androidx.compose.runtime.*
import io.ktor.websocket.Frame
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.auth.AuthScreen
import rs.edu.raf.rma.auth.AuthViewModel
import rs.edu.raf.rma.catalog.CatalogScreen
import rs.edu.raf.rma.catalog.CatalogViewModel
import rs.edu.raf.rma.core.auth.AuthStore
import rs.edu.raf.rma.core.auth.model.AuthState

@Composable
fun ShowtimeApp() {
    val authStore: AuthStore = koinInject()
    val authState by authStore.authState.collectAsState()

    when (authState) {
        is AuthState.Unauthenticated -> {
            AuthScreen(viewModel = koinViewModel<AuthViewModel>())
        }
        is AuthState.Authenticated -> {
            CatalogScreen(viewModel = koinViewModel<CatalogViewModel>())
        }
    }
}