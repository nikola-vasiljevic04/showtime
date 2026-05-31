package rs.edu.raf.rma.core.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import rs.edu.raf.rma.core.db.entities.*

@Database(
    entities = [
        MovieEntity::class,
        UserStatsEntity::class,
        GenreEntity::class,
        MovieDetailsEntity::class,
        FavoriteEntity::class,
        WatchlistEntity::class
               ],
    version = 5,
    exportSchema = false,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun showtimeDao(): ShowtimeDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun buildAppDatabase(
    builder: RoomDatabase.Builder<AppDatabase>,
): AppDatabase {
    return builder
        .fallbackToDestructiveMigration(dropAllTables = true)
        //.fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
