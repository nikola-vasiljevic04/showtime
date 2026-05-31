package rs.edu.raf.rma.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
@Composable
fun ImagesGallerySection(backdrops: List<String>) {
    if (backdrops.isEmpty()) return
    Column(modifier = Modifier.padding(vertical = 16.dp).offset(y = (-20).dp)) {
        Text(text = "IMAGES", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            backdrops.take(5).forEach { imageUrl ->
                AsyncImage(
                    model = imageUrl, contentDescription = "Movie Image", contentScale = ContentScale.Crop,
                    modifier = Modifier.height(120.dp).aspectRatio(16f / 9f).clip(MaterialTheme.shapes.medium).background(MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}