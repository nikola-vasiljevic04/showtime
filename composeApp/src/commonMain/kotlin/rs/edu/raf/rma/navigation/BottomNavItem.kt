package rs.edu.raf.rma.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Catalog : BottomNavItem("catalog_tab", Icons.Filled.Movie, "Movies")
    data object Favorites : BottomNavItem("favorites_tab", Icons.Filled.Favorite, "Favorites")
    data object Watchlist : BottomNavItem("watchlist_tab", Icons.Filled.Bookmark, "Watchlist")
    data object Quiz : BottomNavItem("quiz_tab", Icons.Filled.Quiz, "Quiz")
    data object Profile : BottomNavItem("profile_tab", Icons.Filled.Person, "Profile")
}