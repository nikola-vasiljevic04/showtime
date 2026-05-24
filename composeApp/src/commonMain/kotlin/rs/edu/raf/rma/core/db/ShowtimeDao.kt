package rs.edu.raf.rma.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowtimeDao {
    // --- FILMOVI ---
    @Query("SELECT * FROM movies")
    fun observeAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun observeFavorites(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE inWatchlist = 1")
    fun observeWatchlist(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovies(movies: List<MovieEntity>)

    // --- KVIZ STATISTIKA ---
    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun observeUserStats(): Flow<UserStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserStats(stats: UserStatsEntity)
}