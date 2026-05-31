package rs.edu.raf.rma.core.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MovieWithDetails(
    @Embedded val movie: MovieEntity,

    @Relation(
        parentColumn = "imdbId",
        entityColumn = "movieId"
    )
    val details: MovieDetailsEntity?
)