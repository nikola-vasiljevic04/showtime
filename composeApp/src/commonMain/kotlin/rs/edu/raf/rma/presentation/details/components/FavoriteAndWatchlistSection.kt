package rs.edu.raf.rma.presentation.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteAndWatchlistSection(
    isFavorite: Boolean,
    inWatchlist: Boolean,
    onFavoriteClick: () -> Unit,
    onWatchlistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-40).dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onFavoriteClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color(0xFFFF3B30) else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isFavorite) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isFavorite) "Favorite" else "Add Favorite")
        }

        Button(
            onClick = onWatchlistClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (inWatchlist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (inWatchlist) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = if (inWatchlist) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (inWatchlist) "In Watchlist" else "Watchlist")
        }
    }
}