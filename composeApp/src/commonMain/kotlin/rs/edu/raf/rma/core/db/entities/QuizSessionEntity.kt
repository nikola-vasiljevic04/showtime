package rs.edu.raf.rma.core.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_sessions")
data class QuizSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val score: Float,
    val timestamp: Long // Opciono, da znaš kada je odigrana (npr. System.currentTimeMillis() ili kotlinx.datetime)
)