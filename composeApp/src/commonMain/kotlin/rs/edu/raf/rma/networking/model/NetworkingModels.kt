package rs.edu.raf.rma.networking.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val items: List<T>,
)

@Serializable
data class AuthRequest(
    val username: String,
    val password: String,
    @SerialName("full_name") val fullName: String? = null
)

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long,
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    @SerialName("full_name") val fullName: String
)

@Serializable
data class GenreApiModel(
    val id: Int,
    val name: String
)

@Serializable
data class MovieDetailsApiModel(
    val imdbId: String,
    val tmdbId: Int? = null,
    val title: String,
    val originalTitle: String? = null,
    val overview: String? = null,
    val tagline: String? = null,
    val releaseDate: String? = null,
    val year: Int? = null,
    val runtime: Int? = null,
    val budget: Long? = null,
    val revenue: Long? = null,
    val languageCode: String? = null,
    val popularity: Float? = null,
    val imdbRating: Float? = null,
    val imdbVotes: Int? = null,
    val tmdbRating: Float? = null,
    val tmdbVotes: Int? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val homepage: String? = null,
    val genres: List<GenreApiModel> = emptyList(),
    val collection: CollectionSummaryApiModel? = null
)

@Serializable
data class CollectionSummaryApiModel(
    val id: Int,
    val name: String,
    val posterPath: String? = null,
    val backdropPath: String? = null
)

@Serializable
data class PersonSummaryApiModel(
    val imdbId: String,
    val name: String,
    val professions: String? = null,
    val department: String? = null,
    val profilePath: String? = null
)

@Serializable
data class MovieImagesApiModel(
    val posters: List<ImageApiModel> = emptyList(),
    val backdrops: List<ImageApiModel> = emptyList(),
    val logos: List<ImageApiModel> = emptyList()
)

@Serializable
data class ImageApiModel(
    val filePath: String,
    val width: Int? = null,
    val height: Int? = null,
    val voteAverage: Float? = null,
    val language: String? = null
)

@Serializable
data class VideoApiModel(
    val key: String,
    val site: String? = null,
    val name: String? = null,
    val type: String? = null,


    val official: Boolean? = null,

    val publishedAt: String? = null
)

@Serializable
data class ProductionCompanyApiModel(
    val id: Int,
    val name: String,
    val logoPath: String? = null,
    val originCountry: String? = null
)

@Serializable
data class PersonDetailResponseApiModel(
    val person: PersonApiModel,
    val movies: List<MovieListItemApiModel> = emptyList()
)
@Serializable
data class MovieListItemApiModel(
    val imdbId: String,
    val title: String,
    val year: Int? = null,
    val imdbRating: Float? = null,
    val imdbVotes: Int? = null,
    val posterPath: String? = null,
    val genres: List<GenreApiModel> = emptyList()
)

@Serializable
data class PersonApiModel(
    val imdbId: String,
    val tmdbId: Int? = null,
    val name: String,
    val birthYear: Int? = null,
    val deathYear: Int? = null,
    val professions: String? = null,
    val department: String? = null,
    val popularity: Float? = null,
    val profilePath: String? = null,
    val gender: Int? = null
)

@Serializable
data class CollectionDetailApiModel(
    val collection: CollectionSummaryApiModel,
    val movies: List<MovieListItemApiModel> = emptyList()
)

@Serializable
data class ConfigEntryApiModel(
    val key: String,
    val value: String
)

@Serializable
data class LeaderboardEntry(
    val rank: Int,
    @SerialName("user_id") val userId: Int,
    val username: String,
    @SerialName("full_name") val fullName: String,
    val score: Float,
    @SerialName("played_at") val playedAt: Long,
    @SerialName("total_plays") val totalPlays: Int
)

@Serializable
data class QuizResultDto(
    val id: Int,
    val category: Int,
    val score: Float,
    @SerialName("played_at") val playedAt: Long
)
@Serializable
data class CastResponseApiModel(
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val items: List<CastMemberApiModel>
)

@Serializable
data class CastMemberApiModel(
    val imdbId: String,
    val name: String,
    val professions: String? = null,
    val department: String? = null,
    val profilePath: String? = null
)
@Serializable
data class PostQuizResultRequest(
    val score: Float,
    val category: Int = 1
)
@Serializable
data class VideoItemApiModel(
    val key: String,
    val site: String? = null,
    val name: String? = null,
    val type: String? = null,
    val official: Boolean = false,
    val publishedAt: String? = null
)

@Serializable
data class ImageItemApiModel(
    val filePath: String,
    val width: Int? = null,
    val height: Int? = null,
    val voteAverage: Float? = null,
    val language: String? = null
)
@Serializable
data class PostQuizResultResponse(
    val result: QuizResultDto,
    val ranking: Int
)