package rs.edu.raf.rma.navigation
sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Catalog : Screen("catalog")
    data object Filters : Screen("filters")
    data object Favorites : Screen("favorites")
    data object Watchlist : Screen("watchlist")
    data object Quiz : Screen("quiz")
    data object Profile : Screen("profile")

    data object MovieDetails : Screen("details/{movieId}") {
        fun createRoute(movieId: String) = "details/$movieId"
    }
}