package rs.edu.raf.rma.presentation.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortBar(
    currentSort: String,
    totalMovies: Int,
    onSortChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box {
            FilterChip(
                selected = false,
                onClick = { expanded = true },
                label = {
                    Text("Sort: $currentSort ⬇")
                },
                shape = CircleShape
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf(
                    "imdb_rating",
                    "year",
                    "title"
                ).forEach { option ->

                    DropdownMenuItem(
                        text = {
                            Text(option)
                        },
                        onClick = {
                            onSortChanged(option)
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(
            text = "$totalMovies movies",
            style = MaterialTheme.typography.bodySmall
        )
    }
}