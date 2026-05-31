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
    val overview: String?,
    val runtime: Int?,
    val backdropPath: String?,
    val budget: Long?,
    val revenue: Long?,
    val language: String?,
    val popularity: Double?,
    val tmdbRating: Float?,
    val trailerKey: String?,
    val backdropsString: String?,
    val castString: String?
)