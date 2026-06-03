package rs.edu.raf.rma.presentation.quiz.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnswerList(
    options: List<String>,
    correctOptionIndex: Int,
    selectedAnswerIndex: Int?,
    isAnswerChecked: Boolean,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, optionText ->
            val containerColor = when {
                !isAnswerChecked -> MaterialTheme.colorScheme.surfaceVariant
                index == correctOptionIndex -> Color(0xFF4CAF50)
                index == selectedAnswerIndex -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }

            val contentColor = when {
                !isAnswerChecked -> MaterialTheme.colorScheme.onSurfaceVariant
                index == correctOptionIndex || index == selectedAnswerIndex -> Color.White
                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            }

            Button(
                onClick = { if (!isAnswerChecked) onAnswerSelected(index) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor,
                    disabledContainerColor = containerColor,
                    disabledContentColor = contentColor
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isAnswerChecked
            ) {
                Text(text = optionText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}