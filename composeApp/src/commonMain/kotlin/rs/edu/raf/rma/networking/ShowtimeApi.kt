package rs.edu.raf.rma.networking
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import rs.edu.raf.rma.networking.model.AuthRequest
import rs.edu.raf.rma.networking.model.AuthResponse
import rs.edu.raf.rma.networking.model.CastResponseApiModel
import rs.edu.raf.rma.networking.model.CollectionDetailApiModel
import rs.edu.raf.rma.networking.model.ConfigEntryApiModel
import rs.edu.raf.rma.networking.model.GenreApiModel
import rs.edu.raf.rma.networking.model.LeaderboardEntry
import rs.edu.raf.rma.networking.model.MovieDetailsApiModel
import rs.edu.raf.rma.networking.model.MovieImagesApiModel
import rs.edu.raf.rma.networking.model.MovieListItemApiModel
import rs.edu.raf.rma.networking.model.PaginatedResponse
import rs.edu.raf.rma.networking.model.PersonDetailResponseApiModel
import rs.edu.raf.rma.networking.model.PersonSummaryApiModel
import rs.edu.raf.rma.networking.model.PostQuizResultRequest
import rs.edu.raf.rma.networking.model.PostQuizResultResponse
import rs.edu.raf.rma.networking.model.ProductionCompanyApiModel
import rs.edu.raf.rma.networking.model.QuizResultDto
import rs.edu.raf.rma.networking.model.UserDto
import rs.edu.raf.rma.networking.model.VideoApiModel

interface ShowtimeApi {

    @POST("auth/signup")
    suspend fun signup(@Body request: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @GET("me")
    suspend fun getProfile(): UserDto
    @GET("movies")
    suspend fun getMovies(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("query") query: String? = null,
        @Query("genre_id") genreId: Int? = null,
        @Query("min_rating") minRating: Float? = null,
        @Query("min_year") minYear: Int? = null,
        @Query("max_year") maxYear: Int? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = "desc"
    ): PaginatedResponse<MovieListItemApiModel>

    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id") id: String): MovieDetailsApiModel

    @GET("movies/{id}/cast")
    suspend fun getMovieCast(
        @Path("id") id: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): PaginatedResponse<PersonSummaryApiModel>

    @GET("movies/{id}/images")
    suspend fun getMovieImages(
        @Path("id") id: String,
        @Query("type") type: String? = null // "poster", "backdrop", or "logo"
    ): MovieImagesApiModel

    @GET("movies/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: String,
        @Query("type") type: String? = null // npr. "Trailer"
    ): List<VideoApiModel>

    @GET("movies/{id}/companies")
    suspend fun getMovieCompanies(@Path("id") id: String): List<ProductionCompanyApiModel>

    @GET("people/{id}")
    suspend fun getPersonDetails(@Path("id") id: String): PersonDetailResponseApiModel

    @GET("genres")
    suspend fun getGenres(): List<GenreApiModel>

    @GET("collections/{id}")
    suspend fun getCollectionDetails(@Path("id") id: Int): CollectionDetailApiModel

    @GET("config")
    suspend fun getConfig(): List<ConfigEntryApiModel>

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

    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") category: Int = 1,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): PaginatedResponse<LeaderboardEntry>

    @POST("leaderboard")
    suspend fun submitQuizResult(@Body request: PostQuizResultRequest): PostQuizResultResponse

    @GET("me/quiz-results")
    suspend fun getMyQuizResults(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): PaginatedResponse<QuizResultDto>
//    @GET("movies/{id}/cast")
//    suspend fun getMovieCast(
//        @Path("id") id: String,
//        @Query("page_size") pageSize: Int = 20
//    ): CastResponseApiModel


}