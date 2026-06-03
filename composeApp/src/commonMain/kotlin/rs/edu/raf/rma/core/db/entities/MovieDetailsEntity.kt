package rs.edu.raf.rma.core.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie_details",
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["imdbId"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieDetailsEntity(
    @PrimaryKey val movieId: String,
    val overview: String? = null,
    val runtime: Int? = null,
    val backdropPath: String? = null,
    val budget: Long? = null,
    val revenue: Long? = null,
    val language: String? = null,
    val popularity: Double? = null,
    val tmdbRating: Float? = null,
    val trailerKey: String? = null,
    val backdropsString: String? = null,
    val castString: String? = null
)