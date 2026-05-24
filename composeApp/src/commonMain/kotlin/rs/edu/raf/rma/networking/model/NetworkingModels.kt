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
data class MovieListItemApiModel(
    val imdbId: String,
    val title: String,
    val year: Int? = null,
    val imdbRating: Float? = null,
    val imdbVotes: Int? = null,
    val posterPath: String? = null
)