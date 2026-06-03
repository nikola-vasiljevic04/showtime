package rs.edu.raf.rma.presentation.quiz

import rs.edu.raf.rma.domain.model.QuizQuestion

interface QuizContract {

    enum class QuizPhase { IDLE, IN_PROGRESS, RESULT }

    data class UiState(
        val phase: QuizPhase = QuizPhase.IDLE,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,

        val questions: List<QuizQuestion> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val remainingTime: Int = 60,

        val selectedAnswerIndex: Int? = null,
        val isAnswerChecked: Boolean = false,

        val correctAnswersCount: Int = 0,
        val finalScore: Float = 0f,
        val timeUsed: Int = 0,

        val showAbandonDialog: Boolean = false
    ) {
        val currentQuestion: QuizQuestion?
            get() = questions.getOrNull(currentQuestionIndex)
    }

    sealed class UiEvent {
        data object StartQuizClicked : UiEvent()
        data class AnswerSelected(val index: Int) : UiEvent()
        data object BackClicked : UiEvent()
        data object ConfirmAbandon : UiEvent()
        data object DismissAbandonDialog : UiEvent()
        data object ResetQuiz : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateBack : SideEffect()
    }
}