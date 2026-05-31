package rs.edu.raf.rma.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.edu.raf.rma.domain.model.Genre
import rs.edu.raf.rma.domain.model.Movie
import rs.edu.raf.rma.domain.model.MovieDetails
import rs.edu.raf.rma.presentation.state.FilterData

interface CatalogRepository {
    fun observeMovies(filters: FilterData, sortBy: String): Flow<List<Movie>>
    suspend fun syncMoviesIfNeeded()
    suspend fun toggleFavorite(movie: Movie)
    suspend fun toggleWatchlist(movie: Movie)
    suspend fun getGenres(): List<Genre>
    fun observeMovieDetails(id: String): Flow<MovieDetails?>
    suspend fun refreshMovieDetails(id: String)
    fun observeFavorites(): Flow<List<Movie>>
    fun observeWatchlist(): Flow<List<Movie>>
    suspend fun syncFavorites()
    suspend fun syncWatchlist()
}