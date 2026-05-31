package rs.edu.raf.rma.presentation.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import rs.edu.raf.rma.domain.model.MovieDetails
@Composable
fun InfoSection(details: MovieDetails) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).offset(y = (-20).dp)) {
        Text(text = "INFO", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoCard(title = "Budget", value = formatMoney(details.budget), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InfoCard(title = "Revenue", value = formatMoney(details.revenue), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InfoCard(title = "Language", value = details.language?.uppercase() ?: "N/A", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            InfoCard(title = "Popularity", value = details.popularity?.toInt()?.toString() ?: "N/A", modifier = Modifier.weight(1f))
        }
    }
}