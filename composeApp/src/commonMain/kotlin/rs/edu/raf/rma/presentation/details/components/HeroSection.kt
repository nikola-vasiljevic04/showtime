package rs.edu.raf.rma.presentation.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
@Composable
fun HeroSection(
    backdropPath: String?,
    trailerKey: String?,
    onBackClicked: () -> Unit,
    onPlayClicked: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
        AsyncImage(
            model = backdropPath,
            contentDescription = "Movie BackDrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().background(Color.DarkGray)
        )
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart).background(Color.Black.copy(alpha = 0.4f), CircleShape)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        if (trailerKey != null) {
            IconButton(
                onClick = { onPlayClicked(trailerKey) },
                modifier = Modifier.size(64.dp).align(Alignment.Center).background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play Trailer", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }
    }
}