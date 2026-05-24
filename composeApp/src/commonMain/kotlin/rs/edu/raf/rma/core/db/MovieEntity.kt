package rs.edu.raf.rma.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val year: Int?,
    val imdbRating: Float?,
    val imdbVotes: Int?,
    val posterPath: String?,
    val genresString: String,
    val isFavorite: Boolean = false,
    val inWatchlist: Boolean = false
)