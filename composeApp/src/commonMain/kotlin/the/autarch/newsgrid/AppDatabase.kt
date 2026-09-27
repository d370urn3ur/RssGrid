package the.autarch.newsgrid

import androidx.compose.runtime.Composable
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import the.autarch.newsgrid.bookmark.data.BookmarkDao
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.channel.data.ChannelDao
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.entry.data.EntryEntity
import the.autarch.newsgrid.entry.data.EntryDao

@Database(
    entities = [
        ChannelEntity::class,
        EntryEntity::class,
        BookmarkEntity::class
    ],
    version = 2
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getChannelDao(): ChannelDao
    abstract fun getEntryDao(): EntryDao
    abstract fun getBookmarkDao(): BookmarkDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

@Composable
expect fun rememberDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .addMigrations(
            MIGRATION_1_2
        )
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// region Migrations

val MIGRATION_1_2 = object: Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE channel ADD COLUMN listOrder INTEGER NOT NULL DEFAULT 0")
    }
}

// endregion