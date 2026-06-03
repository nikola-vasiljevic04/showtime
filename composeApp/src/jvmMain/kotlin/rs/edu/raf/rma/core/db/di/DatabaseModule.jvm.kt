package rs.edu.raf.rma.core.db.di

import org.koin.dsl.module
import rs.edu.raf.rma.core.db.AppDatabase
import rs.edu.raf.rma.core.db.buildAppDatabase
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

actual fun databaseModule() = module {
    single<AppDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "showtime.db")
        val builder = Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())

        buildAppDatabase(builder)
    }
    single { get<AppDatabase>().showtimeDao() }
    single { get<AppDatabase>().quizDao() }
}