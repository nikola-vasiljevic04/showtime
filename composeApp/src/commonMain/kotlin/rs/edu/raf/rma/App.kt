package rs.edu.raf.rma

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.navigation.ShowtimeNavigation
import rs.edu.raf.rma.splash.BootState
import rs.edu.raf.rma.splash.SplashScreen
import rs.edu.raf.rma.splash.SplashViewModel
import rs.edu.raf.rma.theme.ShowtimeTheme

@Composable
fun ShowtimeApp() {
    val systemTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemTheme) }
    ShowtimeTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val splashViewModel = koinViewModel<SplashViewModel>()
            val bootState by splashViewModel.bootState.collectAsState()
            val isLoggedIn by splashViewModel.isLoggedIn.collectAsState()

            when (bootState) {
                is BootState.Loading -> {
                    SplashScreen()
                }
                is BootState.Failed -> {
                    Text("Greška pri pokretanju aplikacije")
                }
                is BootState.Success -> {
                    val startDest = if (isLoggedIn) "main" else "auth_landing"

                    ShowtimeNavigation(
                        startDestination = startDest,
                        isLoggedIn = isLoggedIn,
                        onThemeToggle = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}
