package rs.edu.raf.rma.core.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MovieWithDetails(
    @Embedded val movie: MovieEntity,
    val isFavorite: Boolean,
    val inWatchlist: Boolean,

    @Relation(
        parentColumn = "imdbId",
        entityColumn = "movieId"
    )
    val details: MovieDetailsEntity?
)