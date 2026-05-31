package rs.edu.raf.rma.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// (Dark Mode)
private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF7A28CB), // Neka tvoja glavna boja (npr. iOS plava)
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
//    background = Color(0xFF000000),
//    surface = Color(0xFF1C1C1E),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFFF453A)
)

// (Light Mode)
private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF7A28CB),
    background = Color(0xFFF2F2F7),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFFF3B30)
)

@Composable
fun ShowtimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}