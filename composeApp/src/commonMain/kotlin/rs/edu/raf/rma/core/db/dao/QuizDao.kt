package rs.edu.raf.rma.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.edu.raf.rma.core.db.entities.MovieDetailsEntity
import rs.edu.raf.rma.core.db.entities.MovieEntity
import rs.edu.raf.rma.core.db.entities.QuizSessionEntity

@Dao
interface QuizDao {
    @Query("SELECT * FROM movies")
    suspend fun getAllMoviesSync(): List<MovieEntity>
    @Query("SELECT * FROM movie_details WHERE movieId = :id")
    suspend fun getMovieDetailsSync(id: String): MovieDetailsEntity?
    @Query("SELECT * FROM movie_details")
    suspend fun getAllMovieDetailsSync(): List<MovieDetailsEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(details: MovieDetailsEntity)
    @Insert
    suspend fun insertQuizSession(session: QuizSessionEntity)

    @Query("SELECT MAX(score) FROM quiz_sessions")
    fun observeBestScore(): Flow<Float?>

    @Query("SELECT COUNT(*) FROM quiz_sessions")
    fun observeGamesPlayed(): Flow<Int>
}