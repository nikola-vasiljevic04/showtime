package rs.edu.raf.rma.presentation.quiz.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AbandonQuizDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Abandon quiz?") },
        text = { Text(text = "Your progress will be lost and you will get 0 points.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Leave")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}