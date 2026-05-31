package rs.edu.raf.rma.core.db.entities

import androidx.room.Embedded

data class MovieWithStatus(
    @Embedded val movie: MovieEntity,
    val isFavorite: Boolean,
    val inWatchlist: Boolean
)