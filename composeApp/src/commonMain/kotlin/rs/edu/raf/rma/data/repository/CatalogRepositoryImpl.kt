package rs.edu.raf.rma.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.edu.raf.rma.core.db.entities.MovieEntity
import rs.edu.raf.rma.core.db.ShowtimeDao
import rs.edu.raf.rma.core.db.entities.FavoriteEntity
import rs.edu.raf.rma.core.db.entities.MovieDetailsEntity
import rs.edu.raf.rma.core.db.entities.WatchlistEntity
import rs.edu.raf.rma.domain.mapper.toDomain
import rs.edu.raf.rma.domain.mapper.toEntity
import rs.edu.raf.rma.domain.model.Genre
import rs.edu.raf.rma.domain.model.Movie
import rs.edu.raf.rma.domain.repository.CatalogRepository
import rs.edu.raf.rma.networking.ShowtimeApi
import rs.edu.raf.rma.networking.model.MovieListItemApiModel
import rs.edu.raf.rma.presentation.state.FilterData
import  rs.edu.raf.rma.domain.model.MovieDetails
class CatalogRepositoryImpl(
    private val api: ShowtimeApi,
    private val dao: ShowtimeDao
) : CatalogRepository {

    override fun observeMovies(filters: FilterData, sortBy: String): Flow<List<Movie>> {
        return dao.observeMovies(
            query = filters.query,
            genreId = filters.genreId ?: 0,
            minRating = filters.minRating,
            minYear = filters.minYear ?: 0,
            maxYear = filters.maxYear ?: 0,
            sortBy = sortBy
        ).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun syncMoviesIfNeeded() {
        if (dao.getMovieCount() > 0) return

        var currentPage = 1
        var hasMorePages = true

        while (hasMorePages) {
            try {
                val response = api.getMovies(page = currentPage, pageSize = 50)
                if (response.items.isEmpty()) {
                    hasMorePages = false
                    break
                }

                val entities = response.items.map { it.toEntity() }
                dao.insertMovies(entities)

                currentPage++
                if (currentPage > response.totalPages) {
                    hasMorePages = false
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                hasMorePages = false
            }
        }
    }

    override suspend fun getGenres(): List<Genre> {
        if (dao.getGenreCount() == 0) {
            try {
                val apiGenres = api.getGenres()
                dao.insertGenres(apiGenres.map { it.toEntity() })
            } catch (e: Exception) {
                if (e is CancellationException) throw e
            }
        }
        return dao.getAllGenres().map { it.toDomain() }
    }



    private fun MovieListItemApiModel.toEntity(): MovieEntity {
        return MovieEntity(
            imdbId = this.imdbId,
            title = this.title,
            year = this.year,
            imdbRating = this.imdbRating,
            imdbVotes = this.imdbVotes,
            posterPath = this.posterPath,
            genresString = this.genres.joinToString(",") { it.name },
            genreIds = this.genres.joinToString(",") { it.id.toString() }
        )
    }
    override fun observeMovieDetails(id: String): Flow<MovieDetails?> {
        return dao.observeMovieDetails(id).map { it?.toDomain() }
    }


    override suspend fun refreshMovieDetails(id: String) {
        coroutineScope {
            val detailsDeferred = async { api.getMovieDetails(id) }
            val castDeferred = async { api.getMovieCast(id, pageSize = 20) }
            val imagesDeferred = async { api.getMovieImages(id, type = "backdrop") }
            val videosDeferred = async { api.getMovieVideos(id, type = "Trailer") }


            val detailsApi = detailsDeferred.await()
            val castApi = castDeferred.await()
            val imagesApi = imagesDeferred.await()
            val videosApi = videosDeferred.await()


            val castString = castApi.items.take(15).joinToString(",") {
                "${it.name}|${it.profilePath ?: "null"}"
            }


            val backdropsString = imagesApi.backdrops.take(10).joinToString(",") { it.filePath }


            val trailerKey = videosApi.firstOrNull { it.official == true }?.key ?: videosApi.firstOrNull()?.key

            val detailsEntity = MovieDetailsEntity(
                movieId = id,
                overview = detailsApi.overview,
                runtime = detailsApi.runtime,
                backdropPath = detailsApi.backdropPath,
                budget = detailsApi.budget,
                revenue = detailsApi.revenue,
                language = detailsApi.languageCode,
                popularity = detailsApi.popularity?.toDouble(),
                tmdbRating = detailsApi.tmdbRating,
                trailerKey = trailerKey,
                backdropsString = backdropsString,
                castString = castString
            )


            dao.insertMovieDetails(detailsEntity)
        }
    }
    override fun observeFavorites(): Flow<List<Movie>> {
        return dao.observeFavorites().map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeWatchlist(): Flow<List<Movie>> {
        return dao.observeWatchlist().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun toggleFavorite(movie: Movie) {
        val entity = FavoriteEntity(movie.id)
        val newState = !movie.isFavorite

        if (newState) dao.insertFavorite(entity) else dao.deleteFavorite(entity)

        try {
            if (newState) api.addFavorite(movie.id) else api.removeFavorite(movie.id)
        } catch (e: Exception) {
            if (newState) dao.deleteFavorite(entity) else dao.insertFavorite(entity)
            throw e
        }
    }

    override suspend fun toggleWatchlist(movie: Movie) {
        val entity = WatchlistEntity(movie.id)
        val newState = !movie.inWatchlist

        if (newState) dao.insertWatchlist(entity) else dao.deleteWatchlist(entity)

        try {
            if (newState) api.addWatchlist(movie.id) else api.removeWatchlist(movie.id)
        } catch (e: Exception) {
            if (newState) dao.deleteWatchlist(entity) else dao.insertWatchlist(entity)
            throw e
        }
    }

    override suspend fun syncFavorites() {
        val apiFavorites = api.getFavorites()
        dao.clearFavorites()
        apiFavorites.forEach {
            dao.insertFavorite(FavoriteEntity(it.imdbId))
        }
    }

    override suspend fun syncWatchlist() {
        val apiWatchlist = api.getWatchlist()
        dao.clearWatchlist()
        apiWatchlist.forEach {
            dao.insertWatchlist(WatchlistEntity(it.imdbId))
        }
    }
}