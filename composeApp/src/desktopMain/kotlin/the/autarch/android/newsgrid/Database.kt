package the.autarch.android.newsgrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.room.*
import java.io.File

@Composable
actual fun rememberDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "news_grid.db")
    return remember {
        Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
    }
}