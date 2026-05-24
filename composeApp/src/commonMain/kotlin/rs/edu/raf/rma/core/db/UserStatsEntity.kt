package rs.edu.raf.rma.core.db
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val bestScore: Float = 0f,
    val gamesPlayed: Int = 0
)