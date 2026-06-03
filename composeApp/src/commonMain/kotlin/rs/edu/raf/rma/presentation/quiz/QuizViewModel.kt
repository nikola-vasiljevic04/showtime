package rs.edu.raf.rma.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import rs.edu.raf.rma.domain.repository.QuizRepository
import kotlin.math.min

class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuizContract.UiState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<QuizContract.SideEffect>()
    val effects = _effects.asSharedFlow()

    private val events = MutableSharedFlow<QuizContract.UiEvent>()

    private var timerJob: Job? = null

    init {
        observeEvents()
    }

    private fun setState(reducer: QuizContract.UiState.() -> QuizContract.UiState) {
        _state.getAndUpdate(reducer)
    }

    fun setEvent(event: QuizContract.UiEvent) {
        viewModelScope.launch { events.emit(event) }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is QuizContract.UiEvent.StartQuizClicked -> startQuiz()
                    is QuizContract.UiEvent.AnswerSelected -> submitAnswer(event.index)
                    is QuizContract.UiEvent.BackClicked -> handleBackClicked()
                    is QuizContract.UiEvent.ConfirmAbandon -> confirmAbandon()
                    is QuizContract.UiEvent.DismissAbandonDialog -> {
                        setState { copy(showAbandonDialog = false) }
                    }
                    is QuizContract.UiEvent.ResetQuiz -> resetToIdle()
                }
            }
        }
    }

    private fun startQuiz() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }

            runCatching { repository.generateQuiz() }
                .onSuccess { questions ->
                    setState {
                        copy(
                            isLoading = false,
                            phase = QuizContract.QuizPhase.IN_PROGRESS,
                            questions = questions,
                            currentQuestionIndex = 0,
                            remainingTime = 60,
                            correctAnswersCount = 0,
                            selectedAnswerIndex = null,
                            isAnswerChecked = false,
                            showAbandonDialog = false
                        )
                    }
                    startTimer()
                }
                .onFailure { error ->
                    setState {
                        copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to generate quiz."
                        )
                    }
                }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.remainingTime > 0 && _state.value.phase == QuizContract.QuizPhase.IN_PROGRESS) {
                delay(1000)
                val newTime = _state.value.remainingTime - 1
                setState { copy(remainingTime = newTime) }

                if (newTime <= 0) {
                    finishQuiz()
                }
            }
        }
    }

    private fun submitAnswer(index: Int) {
        val currentState = _state.value

        if (currentState.isAnswerChecked || currentState.phase != QuizContract.QuizPhase.IN_PROGRESS || currentState.remainingTime <= 0) return

        val isCorrect = index == currentState.currentQuestion?.correctOptionIndex
        val newCorrectCount = if (isCorrect) currentState.correctAnswersCount + 1 else currentState.correctAnswersCount

        setState {
            copy(
                selectedAnswerIndex = index,
                isAnswerChecked = true,
                correctAnswersCount = newCorrectCount
            )
        }

        viewModelScope.launch {
            delay(1500)

            val nextIndex = currentState.currentQuestionIndex + 1
            if (nextIndex < currentState.questions.size && _state.value.remainingTime > 0) {
                setState {
                    copy(
                        currentQuestionIndex = nextIndex,
                        selectedAnswerIndex = null,
                        isAnswerChecked = false
                    )
                }
            } else {
                finishQuiz()
            }
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()

        val currentState = _state.value
        if (currentState.phase == QuizContract.QuizPhase.RESULT) return

        val bto = currentState.correctAnswersCount
        val pvt = currentState.remainingTime
        val mvt = 60f

        val calculatedScore = bto * (9f + (pvt / mvt))
        val finalScore = min(calculatedScore, 100f)
        val timeUsed = 60 - pvt

        setState {
            copy(
                phase = QuizContract.QuizPhase.RESULT,
                finalScore = finalScore,
                timeUsed = timeUsed,
                showAbandonDialog = false
            )
        }

        viewModelScope.launch {
            repository.updateLocalStats(finalScore)
            repository.submitScore(finalScore)
        }
    }

    private fun handleBackClicked() {
        if (_state.value.phase == QuizContract.QuizPhase.IN_PROGRESS) {
            setState { copy(showAbandonDialog = true) }
        } else {
            viewModelScope.launch { _effects.emit(QuizContract.SideEffect.NavigateBack) }
        }
    }

    private fun confirmAbandon() {
        timerJob?.cancel()
        setState { copy(showAbandonDialog = false) }
        viewModelScope.launch { _effects.emit(QuizContract.SideEffect.NavigateBack) }
    }

    private fun resetToIdle() {
        timerJob?.cancel()
        setState {
            copy(
                phase = QuizContract.QuizPhase.IDLE,
                questions = emptyList(),
                currentQuestionIndex = 0,
                remainingTime = 60,
                selectedAnswerIndex = null,
                isAnswerChecked = false,
                correctAnswersCount = 0,
                showAbandonDialog = false
            )
        }
        viewModelScope.launch { _effects.emit(QuizContract.SideEffect.NavigateBack) }
    }
}