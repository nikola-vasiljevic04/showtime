package rs.edu.raf.rma.presentation.quiz.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizResultContent(
    score: Float,
    correctAnswers: Int,
    timeUsed: Int,
    onDoneClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz Finished!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Your Score", fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        Text(
            text = "${score.toInt()}",
            fontSize = 64.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text("Correct Answers: $correctAnswers / 10", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Time Used: $timeUsed seconds", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onDoneClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Done", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}