package rs.edu.raf.rma.presentation.quiz

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import rs.edu.raf.rma.presentation.quiz.components.*
import rs.edu.raf.rma.utils.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onNavigateBack: () -> Unit,
    onQuizActiveChange: (Boolean) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is QuizContract.SideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    LaunchedEffect(state.phase) {
        onQuizActiveChange(state.phase != QuizContract.QuizPhase.IDLE)
    }
    BackHandler(enabled = state.phase == QuizContract.QuizPhase.IN_PROGRESS) {
        viewModel.setEvent(QuizContract.UiEvent.BackClicked)
    }

    if (state.showAbandonDialog) {
        AbandonQuizDialog(
            onConfirm = { viewModel.setEvent(QuizContract.UiEvent.ConfirmAbandon) },
            onDismiss = { viewModel.setEvent(QuizContract.UiEvent.DismissAbandonDialog) }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                return@Box
            }

            if (state.errorMessage != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.errorMessage!!,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.setEvent(QuizContract.UiEvent.BackClicked) }) {
                        Text("Go Back")
                    }
                }
                return@Box
            }

            when (state.phase) {
                QuizContract.QuizPhase.IDLE -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Showtime Quiz",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "You have 60 seconds to answer 10 questions.\nAre you ready?",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                        Button(
                            onClick = { viewModel.setEvent(QuizContract.UiEvent.StartQuizClicked) },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Text("Start Quiz", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                QuizContract.QuizPhase.IN_PROGRESS -> {
                    val question = state.currentQuestion ?: return@Box

                    Column(modifier = Modifier.fillMaxSize()) {
                        QuizTopBar(
                            currentQuestionIndex = state.currentQuestionIndex,
                            totalQuestions = state.questions.size,
                            remainingTime = state.remainingTime
                        )

                        AnimatedContent(
                            targetState = state.currentQuestionIndex,
                            transitionSpec = {
                                slideInHorizontally(animationSpec = tween(400)) { width -> width } + fadeIn() togetherWith
                                        slideOutHorizontally(animationSpec = tween(400)) { width -> -width } + fadeOut()
                            },
                            modifier = Modifier.weight(1f),
                            label = "question_animation"
                        ) { _ ->
                            Column(
                                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = question.title,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = question.questionText,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth().height(200.dp),
                                    shape = MaterialTheme.shapes.large
                                ) {
                                    AsyncImage(
                                        model = question.imageUrl,
                                        contentDescription = "Quiz Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                AnswerList(
                                    options = question.options,
                                    correctOptionIndex = question.correctOptionIndex,
                                    selectedAnswerIndex = state.selectedAnswerIndex,
                                    isAnswerChecked = state.isAnswerChecked,
                                    onAnswerSelected = { index ->
                                        viewModel.setEvent(QuizContract.UiEvent.AnswerSelected(index))
                                    }
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }
                    }
                }

                QuizContract.QuizPhase.RESULT -> {
                    QuizResultContent(
                        score = state.finalScore,
                        correctAnswers = state.correctAnswersCount,
                        timeUsed = state.timeUsed,
                        onDoneClicked = { viewModel.setEvent(QuizContract.UiEvent.ResetQuiz) }
                    )
                }
            }
        }
    }
}