package rs.edu.raf.rma.presentation.quiz.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizTopBar(
    currentQuestionIndex: Int,
    totalQuestions: Int,
    remainingTime: Int,
    modifier: Modifier = Modifier
) {
    val progress = remainingTime / 60f

    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question ${currentQuestionIndex + 1}/$totalQuestions",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "00:${remainingTime.toString().padStart(2, '0')}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (remainingTime <= 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = if (remainingTime <= 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}