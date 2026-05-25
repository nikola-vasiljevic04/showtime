package rs.edu.raf.rma.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import rs.edu.raf.rma.core.db.MovieEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(viewModel: CatalogViewModel) {
    val state by viewModel.uiState.collectAsState()

    val pureBlack = Color(0xFF000000)
    val darkGray = Color(0xFF1C1C1E)
    val textMuted = Color(0xFF8E8E93)
    val iosBlue = Color(0xFF0A84FF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SHOWTIME", fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = pureBlack)
            )
        },
        containerColor = pureBlack
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Loading indikator kada je lista potpuno prazna
            if (state.isLoading && state.movies.isEmpty()) {
                CircularProgressIndicator(color = iosBlue)
            }
            // Prikaz greške
            else if (state.error != null && state.movies.isEmpty()) {
                Text("Greška: ${state.error}", color = Color.Red)
            }
            // Prikaz filmova iz baze
            else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // 2 filma u redu
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.movies) { movie ->
                        MovieCard(movie, darkGray, textMuted)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: MovieEntity, backgroundColor: Color, textMuted: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth().aspectRatio(0.65f) // Odnos širine i visine sličan posteru
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Konstrusišemo Full URL za sliku sa API-ja prema specifikaciji (w500 veličina)
            val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.DarkGray)
            ) {
                // Coil3 asinhrono učitavanje slike
                AsyncImage(
                    model = imageUrl,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop, // Seče sliku da savršeno popuni Box
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFD60A), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${movie.imdbRating ?: "N/A"}",
                        color = textMuted,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${movie.year ?: ""}",
                        color = textMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}