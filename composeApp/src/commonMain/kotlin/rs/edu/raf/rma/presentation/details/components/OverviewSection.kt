package rs.edu.raf.rma.presentation.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun OverviewSection(overview: String?) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).offset(y = (-30).dp)) {
        Text(text = "OVERVIEW", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = overview ?: "No overview available.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 22.sp
        )
    }
}