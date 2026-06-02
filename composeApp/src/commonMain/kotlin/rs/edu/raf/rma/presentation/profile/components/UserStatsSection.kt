package rs.edu.raf.rma.presentation.profile.components


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserStatsSection(
    favoritesCount: Int,
    watchlistCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Favorites",
            count = favoritesCount,
            icon = Icons.Filled.Favorite,
            tint = Color.Red
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Watchlist",
            count = watchlistCount,
            icon = Icons.Filled.Bookmark,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
