package rs.edu.raf.rma.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.presentation.catalog.CatalogScreen
import rs.edu.raf.rma.presentation.catalog.CatalogViewModel
import rs.edu.raf.rma.presentation.filters.FiltersScreen
import rs.edu.raf.rma.presentation.filters.FiltersViewModel
import rs.edu.raf.rma.auth.AuthScreen
import rs.edu.raf.rma.auth.AuthViewModel
import rs.edu.raf.rma.presentation.details.MovieDetailsScreen
import rs.edu.raf.rma.presentation.details.MovieDetailsViewModel

const val MOVIE_ID_ARG = "movieId"

@Composable
fun ShowtimeNavigation(
    startDestination: String,
    onThemeToggle: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = "auth_landing") {
            val viewModel = koinViewModel<AuthViewModel>()
            AuthScreen(
                viewModel = viewModel,
                onNavigateToCatalog = {
                    // Kad auth uspe, prebacujemo se na katalog i brišemo auth iz backstack-a!
                    navController.navigate("catalog") {
                        popUpTo("auth_landing") { inclusive = true }
                    }
                }
            )
        }
        composable(route = "catalog") {
            val viewModel = koinViewModel<CatalogViewModel>()
            CatalogScreen(
                viewModel = viewModel,
                onThemeToggle = onThemeToggle,
                onNavigateToDetails = { navController.navigateToMovieDetails(movieId = it) },
                onNavigateToFilters = { navController.navigate("filters") }
            )
        }

        composable(route = "filters") {
            val viewModel = koinViewModel<FiltersViewModel>()
            FiltersScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = "details/{movieId}",
            arguments = listOf(
                navArgument("movieId") { type = NavType.StringType }
            )
        ) {
            // Koin će automatski ubaciti "movieId" iz Bundle-a u SavedStateHandle
            val viewModel = koinViewModel<MovieDetailsViewModel>()

            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

// Ekstenzione funkcije za sigurniju navigaciju (kao kod njih na vežbama)
private fun NavController.navigateToMovieDetails(movieId: String) {
    navigate("details/$movieId")
}