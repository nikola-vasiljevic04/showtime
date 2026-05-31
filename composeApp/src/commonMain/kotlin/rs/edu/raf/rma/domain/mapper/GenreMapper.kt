package rs.edu.raf.rma.domain.mapper

import rs.edu.raf.rma.core.db.entities.GenreEntity
import rs.edu.raf.rma.domain.model.Genre
import rs.edu.raf.rma.networking.model.GenreApiModel

fun GenreEntity.toDomain(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}

fun GenreApiModel.toEntity(): GenreEntity {
    return GenreEntity(
        id = this.id,
        name = this.name
    )
}