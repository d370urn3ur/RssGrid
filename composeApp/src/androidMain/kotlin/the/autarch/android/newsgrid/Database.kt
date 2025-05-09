package the.autarch.android.newsgrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Composable
actual fun rememberDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = LocalContext.current.applicationContext
    val dbFile = appContext.getDatabasePath("news_grid.db")
    return remember {
        Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    db.query("PRAGMA foreign_keys = ON;")
                }
            })
    }
}