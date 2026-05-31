package rs.edu.raf.rma.domain.model

data class MovieDetails(
    val movie: Movie,
    val overview: String?,
    val runtime: Int?,
    val backdropUrl: String?,
    val budget: Long?,
    val revenue: Long?,
    val language: String?,
    val popularity: Double?,
    val tmdbRating: Float?,
    val trailerKey: String?,
    val backdrops: List<String>,
    val cast: List<CastMember>
)