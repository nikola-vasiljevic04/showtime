package rs.edu.raf.rma.domain.repository

import rs.edu.raf.rma.domain.model.QuizQuestion

interface QuizRepository {
    suspend fun generateQuiz(): List<QuizQuestion>
    suspend fun submitScore(score: Float)
    suspend fun updateLocalStats(score: Float)
}