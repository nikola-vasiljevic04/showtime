package rs.edu.raf.rma.domain.model

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val rating: String,
    val votes: String,
    val posterUrl: String,
    val genres: List<String>,
    val isFavorite: Boolean,
    val inWatchlist: Boolean
)