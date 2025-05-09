package the.autarch.android.newsgrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.room.*
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*

@Composable
actual fun rememberDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/news_grid.db"
    return remember {
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}