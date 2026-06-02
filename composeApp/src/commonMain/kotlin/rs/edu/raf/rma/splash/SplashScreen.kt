package rs.edu.raf.rma.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AppAnimatedLogo()
        }
    }
}

@Composable
private fun AppAnimatedLogo() {
    val infiniteTransition = rememberInfiniteTransition()
    val pulsate by infiniteTransition.animateFloat(
        initialValue = 32f,
        targetValue = 48f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
    )

    Text(
        text = "Showtime",
        color = MaterialTheme.colorScheme.onPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = pulsate.sp
    )
}