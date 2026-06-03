package rs.edu.raf.rma.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import rs.edu.raf.rma.presentation.catalog.CatalogScreen
import rs.edu.raf.rma.presentation.catalog.CatalogViewModel
import rs.edu.raf.rma.presentation.favorites.FavoritesScreen
import rs.edu.raf.rma.presentation.favorites.FavoritesViewModel
import rs.edu.raf.rma.presentation.quiz.QuizScreen
import rs.edu.raf.rma.presentation.quiz.QuizViewModel
import rs.edu.raf.rma.presentation.watchlist.WatchlistScreen
import rs.edu.raf.rma.presentation.watchlist.WatchlistViewModel

@Composable
fun MainScreen(
    onThemeToggle: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToFilters: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Catalog,
        BottomNavItem.Favorites,
        BottomNavItem.Watchlist,
        BottomNavItem.Quiz,
        BottomNavItem.Profile
    )
    var isQuizActive by remember { mutableStateOf(false) }
    val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute != BottomNavItem.Quiz.route || !isQuizActive
    Scaffold(
        bottomBar = {
            if (showBottomBar){
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                bottomNavController.navigate(item.route) {
                                    popUpTo(bottomNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
        }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Catalog.route,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(BottomNavItem.Catalog.route) {
                CatalogScreen(
                    viewModel = koinViewModel<CatalogViewModel>(),
                    onThemeToggle = onThemeToggle,
                    onNavigateToDetails = onNavigateToDetails,
                    onNavigateToFilters = onNavigateToFilters
                )
            }
            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen(
                    viewModel = koinViewModel<FavoritesViewModel>(),
                    onNavigateToDetails = onNavigateToDetails
                )
            }
            composable(BottomNavItem.Watchlist.route) {
                WatchlistScreen(
                    viewModel = koinViewModel<WatchlistViewModel>(),
                    onNavigateToDetails = onNavigateToDetails
                )
            }
            composable(BottomNavItem.Quiz.route) {
                QuizScreen(
                    viewModel = koinViewModel<QuizViewModel>(),
                    onNavigateBack = {
                        bottomNavController.navigate(BottomNavItem.Catalog.route) {
                            popUpTo(BottomNavItem.Catalog.route) { inclusive = true }
                        }
                    },onQuizActiveChange = { isActive ->
                        isQuizActive = isActive
                    }
                )
            }
            composable(BottomNavItem.Profile.route) {
                rs.edu.raf.rma.presentation.profile.ProfileScreen(
                    viewModel = koinViewModel<rs.edu.raf.rma.presentation.profile.ProfileViewModel>()
                )
            }
        }
    }
}