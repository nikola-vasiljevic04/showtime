package rs.edu.raf.rma.presentation.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.round
@Composable
fun InfoCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)) {
        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}

fun formatMoney(amount: Long?): String {
    if (amount == null || amount <= 0) return "N/A"
    return when {
        amount >= 1_000_000_000 -> "$${(round((amount / 1_000_000_000f) * 10) / 10).toString().removeSuffix(".0")}B"
        amount >= 1_000_000 -> "$${(round((amount / 1_000_000f) * 10) / 10).toString().removeSuffix(".0")}M"
        amount >= 1_000 -> "$${(round((amount / 1_000f) * 10) / 10).toString().removeSuffix(".0")}K"
        else -> "$$amount"
    }
}