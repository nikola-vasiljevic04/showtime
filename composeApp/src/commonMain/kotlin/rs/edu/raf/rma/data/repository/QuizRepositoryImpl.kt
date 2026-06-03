package rs.edu.raf.rma.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import rs.edu.raf.rma.core.db.dao.QuizDao
import rs.edu.raf.rma.core.db.entities.MovieDetailsEntity
import rs.edu.raf.rma.core.db.entities.MovieEntity
import rs.edu.raf.rma.core.db.entities.QuizSessionEntity
import rs.edu.raf.rma.domain.model.QuizQuestion
import rs.edu.raf.rma.domain.repository.QuizRepository
import rs.edu.raf.rma.networking.ShowtimeApi
import rs.edu.raf.rma.networking.model.PostQuizResultRequest
import kotlin.time.Clock

class QuizRepositoryImpl(
    private val dao: QuizDao,
    private val api: ShowtimeApi
) : QuizRepository {

    private enum class QType { MOVIE, YEAR, ACTOR }

    private val fallbackActors = listOf(
        "Leonardo DiCaprio", "Brad Pitt", "Tom Hanks", "Robert De Niro", "Al Pacino",
        "Morgan Freeman", "Matt Damon", "Christian Bale", "Johnny Depp", "Denzel Washington",
        "Meryl Streep", "Scarlett Johansson", "Natalie Portman", "Emma Stone", "Angelina Jolie"
    )

    override suspend fun generateQuiz(): List<QuizQuestion> {
        val allMovies = dao.getAllMoviesSync()

        val moviesWithImagesCount = allMovies.count { !it.posterPath.isNullOrBlank() }
        if (moviesWithImagesCount < 10) {
            throw IllegalStateException("Browse the catalog first to populate your quiz pool.")
        }

        ensureMovieDetailsExist(allMovies)

        val allDetailsList = dao.getAllMovieDetailsSync()
        if (allDetailsList.isEmpty()) {
            throw IllegalStateException("Browse the catalog first to populate your quiz pool.")
        }

        val moviesMap = allMovies.associateBy { it.imdbId }

        val questions = mutableListOf<QuizQuestion>()
        val usedImages = mutableSetOf<String>()
        val usedMovieIds = mutableSetOf<String>()
        val typeCounts = mutableMapOf(QType.MOVIE to 0, QType.YEAR to 0, QType.ACTOR to 0)

        var attempts = 0
        while (questions.size < 10 && attempts < 100) {
            attempts++
            val availableTypes = QType.entries.filter { typeCounts[it]!! < 4 }.shuffled()
            var questionAdded = false

            for (type in availableTypes) {
                val q = tryGenerateQuestion(type, allDetailsList, moviesMap, usedImages, usedMovieIds)
                if (q != null) {
                    questions.add(q)
                    usedImages.add(q.imageUrl)
                    typeCounts[type] = typeCounts[type]!! + 1
                    questionAdded = true
                    break
                }
            }
            if (!questionAdded) break
        }

        if (questions.size < 10) {
            throw IllegalStateException("Not enough unique movie data to generate 10 questions. Try browsing the catalog more.")
        }

        return questions
    }

    private fun tryGenerateQuestion(
        type: QType,
        detailsList: List<MovieDetailsEntity>,
        moviesMap: Map<String, MovieEntity>,
        usedImages: Set<String>,
        usedMovieIds: MutableSet<String>
    ): QuizQuestion? {

        val detail = detailsList.shuffled().firstOrNull { d ->
            val movie = moviesMap[d.movieId] ?: return@firstOrNull false
            if (usedMovieIds.contains(movie.imdbId)) return@firstOrNull false

            val poster = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
            val backdrop = d.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }

            (poster != null && !usedImages.contains(poster)) ||
                    (backdrop != null && !usedImages.contains(backdrop))
        } ?: return null

        val movie = moviesMap[detail.movieId] ?: return null

        val posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
        val backdropUrl = detail.backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" } ?: posterUrl

        if (posterUrl == null && backdropUrl == null) return null

        return when (type) {
            QType.MOVIE -> {
                val imageUrl = if (backdropUrl != null && !usedImages.contains(backdropUrl)) backdropUrl else posterUrl!!

                val wrongOptions = detailsList.filter { it.movieId != detail.movieId }
                    .mapNotNull { moviesMap[it.movieId]?.title }
                    .distinct()
                    .shuffled()
                    .take(3)

                if (wrongOptions.size < 3) return null

                usedMovieIds.add(movie.imdbId)
                val options = (wrongOptions + movie.title).shuffled()
                QuizQuestion.GuessMovie(imageUrl, options, options.indexOf(movie.title))
            }

            QType.YEAR -> {
                val imageUrl = posterUrl ?: backdropUrl!!
                val correctYear = movie.year ?: return null

                val wrongOptions = mutableSetOf<Int>()
                var yearAttempts = 0
                while (wrongOptions.size < 3 && yearAttempts < 100) {
                    yearAttempts++
                    val offset = (-10..10).filter { it != 0 }.random()
                    val fakeYear = correctYear + offset
                    if (fakeYear <= 2026 && fakeYear != correctYear) {
                        wrongOptions.add(fakeYear)
                    }
                }

                if (wrongOptions.size < 3) return null

                usedMovieIds.add(movie.imdbId)
                val options = (wrongOptions.map { it.toString() } + correctYear.toString()).shuffled()
                QuizQuestion.GuessYear(movie.title, imageUrl, options, options.indexOf(correctYear.toString()))
            }

            QType.ACTOR -> {
                val imageUrl = posterUrl ?: backdropUrl!!

                val castList = detail.castString?.split(",")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.split("|")[0] }
                    ?: return null

                if (castList.isEmpty()) return null

                val correctActor = castList.take(3).random()

                var allOtherActors = detailsList.filter { it.movieId != detail.movieId }
                    .flatMap { it.castString?.split(",")?.map { cast -> cast.split("|")[0] } ?: emptyList() }
                    .filter { it.isNotBlank() && it != correctActor && !castList.contains(it) }
                    .distinct()

                if (allOtherActors.size < 3) {
                    allOtherActors = (allOtherActors + fallbackActors)
                        .filter { it != correctActor && !castList.contains(it) }
                        .distinct()
                }

                if (allOtherActors.size < 3) return null

                usedMovieIds.add(movie.imdbId)
                val options = (allOtherActors.shuffled().take(3) + correctActor).shuffled()
                QuizQuestion.GuessActor(movie.title, imageUrl, options, options.indexOf(correctActor))
            }
        }
    }

    private suspend fun ensureMovieDetailsExist(movies: List<MovieEntity>) {
        val existingDetailsCount = dao.getAllMovieDetailsSync().size
        if (existingDetailsCount >= 40) return

        val moviesWithoutDetails = movies.filter { dao.getMovieDetailsSync(it.imdbId) == null }
        val toFetch = moviesWithoutDetails.shuffled().take(40 - existingDetailsCount)

        if (toFetch.isEmpty()) return

        coroutineScope {
            toFetch.map { movie ->
                async {
                    runCatching {
                        val apiDetails = api.getMovieDetails(movie.imdbId)
                        val castApi = api.getMovieCast(movie.imdbId, pageSize = 20)

                        val castString = castApi.items.take(15).joinToString(",") {
                            "${it.name}|${it.profilePath ?: "null"}"
                        }

                        val entity = MovieDetailsEntity(
                            movieId = movie.imdbId,
                            backdropPath = apiDetails.backdropPath,
                            castString = castString
                        )
                        dao.insertMovieDetails(entity)
                    }
                }
            }.awaitAll()
        }
    }

    override suspend fun submitScore(score: Float) {
        try {
            api.submitQuizResult(PostQuizResultRequest(score = score))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
        }
    }

    override suspend fun updateLocalStats(score: Float) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        val newSession = QuizSessionEntity(score = score, timestamp = timestamp)
        dao.insertQuizSession(newSession)
    }
}