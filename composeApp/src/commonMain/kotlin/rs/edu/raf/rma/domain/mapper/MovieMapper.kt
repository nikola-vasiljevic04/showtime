package rs.edu.raf.rma.domain.mapper

import rs.edu.raf.rma.core.db.entities.MovieEntity
import rs.edu.raf.rma.core.db.entities.MovieWithDetails
import rs.edu.raf.rma.core.db.entities.MovieWithStatus
import rs.edu.raf.rma.domain.model.CastMember
import rs.edu.raf.rma.domain.model.Movie
import rs.edu.raf.rma.domain.model.MovieDetails
import rs.edu.raf.rma.presentation.catalog.utils.formatVotes
import kotlin.math.round

fun MovieEntity.toDomain(isFavorite: Boolean, inWatchlist: Boolean): Movie {
    return Movie(
        id = this.imdbId,
        title = this.title,
        year = this.year?.toString() ?: "",
        rating = this.imdbRating?.toString() ?: "N/A",
        votes = formatVotes(this.imdbVotes),
        posterUrl = this.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        genres = this.genresString.split(",").filter { it.isNotBlank() },
        isFavorite = isFavorite,
        inWatchlist = inWatchlist
    )
}
fun MovieWithDetails.toDomain(): MovieDetails {
    return MovieDetails(
        movie = this.movie.toDomain(isFavorite = false, inWatchlist = false),
        overview = this.details?.overview,
        runtime = this.details?.runtime,
        backdropUrl = this.details?.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" },
        budget = this.details?.budget,
        revenue = this.details?.revenue,
        language = this.details?.language,
        popularity = this.details?.popularity,
        tmdbRating = this.details?.tmdbRating,
        trailerKey = this.details?.trailerKey,
        backdrops = this.details?.backdropsString?.split(",")?.filter { it.isNotBlank() }?.map { "https://image.tmdb.org/t/p/w780$it" } ?: emptyList(),
        cast = this.details?.castString?.split(",")?.filter { it.isNotBlank() }?.map {
            val parts = it.split("|")
            CastMember(
                name = parts.getOrNull(0) ?: "Nepoznato",
                profileUrl = parts.getOrNull(1)?.takeIf { path -> path != "null" }
                    ?.let { path -> "https://image.tmdb.org/t/p/w185$path" }
            )
        } ?: emptyList()
    )
}
fun MovieWithStatus.toDomain(): Movie {
    return this.movie.toDomain(
        isFavorite = this.isFavorite,
        inWatchlist = this.inWatchlist
    )
}