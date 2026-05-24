package rs.edu.raf.rma.networking
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import rs.edu.raf.rma.networking.model.AuthRequest
import rs.edu.raf.rma.networking.model.AuthResponse
import rs.edu.raf.rma.networking.model.MovieListItemApiModel
import rs.edu.raf.rma.networking.model.PaginatedResponse
import rs.edu.raf.rma.networking.model.UserDto

interface ShowtimeApi {

    @POST("auth/signup")
    suspend fun signup(@Body request: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @GET("me")
    suspend fun getProfile(): UserDto

    @GET("me/favorites")
    suspend fun getFavorites(): List<MovieListItemApiModel>

    @POST("me/favorites/{movie_id}")
    suspend fun addFavorite(@Path("movie_id") movieId: String)

    @DELETE("me/favorites/{movie_id}")
    suspend fun removeFavorite(@Path("movie_id") movieId: String)

    @GET("me/watchlist")
    suspend fun getWatchlist(): List<MovieListItemApiModel>

    @POST("me/watchlist/{movie_id}")
    suspend fun addWatchlist(@Path("movie_id") movieId: String)

    @DELETE("me/watchlist/{movie_id}")
    suspend fun removeWatchlist(@Path("movie_id") movieId: String)

    @GET("movies")
    suspend fun getMovies(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("query") query: String? = null
    ): PaginatedResponse<MovieListItemApiModel>
}