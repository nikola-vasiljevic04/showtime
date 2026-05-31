package rs.edu.raf.rma.core.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import rs.edu.raf.rma.core.db.entities.FavoriteEntity
import rs.edu.raf.rma.core.db.entities.GenreEntity
import rs.edu.raf.rma.core.db.entities.MovieDetailsEntity
import rs.edu.raf.rma.core.db.entities.MovieEntity
import rs.edu.raf.rma.core.db.entities.MovieWithDetails
import rs.edu.raf.rma.core.db.entities.MovieWithStatus
import rs.edu.raf.rma.core.db.entities.WatchlistEntity

@Dao
interface ShowtimeDao {

    @Query("""
        SELECT m.*, 
               CASE WHEN f.movieId IS NOT NULL THEN 1 ELSE 0 END AS isFavorite,
               CASE WHEN w.movieId IS NOT NULL THEN 1 ELSE 0 END AS inWatchlist
        FROM movies m
        LEFT JOIN favorites f ON m.imdbId = f.movieId
        LEFT JOIN watchlist w ON m.imdbId = w.movieId
        WHERE m.title LIKE '%' || :query || '%'
        AND m.imdbRating >= :minRating
        AND (m.year >= :minYear OR :minYear = 0)
        AND (m.year <= :maxYear OR :maxYear = 0)
        AND (:genreId = 0 OR m.genreIds LIKE '%' || :genreId || '%')
        ORDER BY 
            CASE WHEN :sortBy = 'imdb_rating' THEN m.imdbRating END DESC,
            CASE WHEN :sortBy = 'year' THEN m.year END DESC,
            CASE WHEN :sortBy = 'title' THEN m.title END ASC
    """)
    fun observeMovies(
        query: String,
        genreId: Int,
        minRating: Float,
        minYear: Int,
        maxYear: Int,
        sortBy: String
    ): Flow<List<MovieWithStatus>> // OBAVEZNO promeniti return tip u MovieWithStatus


    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    @Query("""
        SELECT m.*, 
               CASE WHEN f.movieId IS NOT NULL THEN 1 ELSE 0 END AS isFavorite,
               CASE WHEN w.movieId IS NOT NULL THEN 1 ELSE 0 END AS inWatchlist
        FROM movies m
        LEFT JOIN favorites f ON m.imdbId = f.movieId
        LEFT JOIN watchlist w ON m.imdbId = w.movieId
    """)
    fun observeMoviesWithStatus(): Flow<List<MovieWithStatus>>

    @Query("""
        SELECT m.*, 1 AS isFavorite, CASE WHEN w.movieId IS NOT NULL THEN 1 ELSE 0 END AS inWatchlist
        FROM movies m
        INNER JOIN favorites f ON m.imdbId = f.movieId
        LEFT JOIN watchlist w ON m.imdbId = w.movieId
    """)
    fun observeFavorites(): Flow<List<MovieWithStatus>>

    @Query("""
        SELECT m.*, CASE WHEN f.movieId IS NOT NULL THEN 1 ELSE 0 END AS isFavorite, 1 AS inWatchlist
        FROM movies m
        LEFT JOIN favorites f ON m.imdbId = f.movieId
        INNER JOIN watchlist w ON m.imdbId = w.movieId
    """)
    fun observeWatchlist(): Flow<List<MovieWithStatus>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlist: WatchlistEntity)

    @Delete
    suspend fun deleteWatchlist(watchlist: WatchlistEntity)

    @Query("DELETE FROM favorites")
    suspend fun clearFavorites()

    @Query("DELETE FROM watchlist")
    suspend fun clearWatchlist()

    @Query("SELECT * FROM genres")
    suspend fun getAllGenres(): List<GenreEntity>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)



    @Query("SELECT COUNT(*) FROM genres")
    suspend fun getGenreCount(): Int



    @Transaction
    @Query("SELECT * FROM movies WHERE imdbId = :id")
    fun observeMovieDetails(id: String): Flow<MovieWithDetails?>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(details: MovieDetailsEntity)
}