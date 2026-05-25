package rs.edu.raf.rma.catalog

import kotlinx.coroutines.flow.Flow
import rs.edu.raf.rma.core.db.MovieEntity
import rs.edu.raf.rma.core.db.ShowtimeDao
import rs.edu.raf.rma.networking.ShowtimeApi
import rs.edu.raf.rma.networking.model.MovieListItemApiModel

class CatalogRepository(
    private val api: ShowtimeApi,
    private val dao: ShowtimeDao
) {
    val moviesStream: Flow<List<MovieEntity>> = dao.observeAllMovies()

    suspend fun fetchAndSaveMovies() {
        val response = api.getMovies(page = 1, pageSize = 20)

        val entities = response.items.map { it.toEntity() }
        dao.upsertMovies(entities)
    }
    private fun MovieListItemApiModel.toEntity(): MovieEntity {
        return MovieEntity(
            imdbId = this.imdbId,
            title = this.title,
            year = this.year,
            imdbRating = this.imdbRating,
            imdbVotes = this.imdbVotes,
            posterPath = this.posterPath,
            genresString = "",
            isFavorite = false,
            inWatchlist = false
        )
    }
}