package rs.edu.raf.rma.presentation.catalog.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogTopBar(
    onThemeToggle: () -> Unit,
    onFilterClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Showtime",
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        actions = {
            IconButton(onClick = onThemeToggle) {
                Icon(
                    Icons.Filled.DarkMode,
                    contentDescription = null
                )
            }

            Button(
                onClick = onFilterClick,
                shape = CircleShape,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Filters")
            }
        }
    )
}