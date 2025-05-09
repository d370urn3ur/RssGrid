package the.autarch.android.newsgrid

import androidx.compose.runtime.Composable
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import the.autarch.android.newsgrid.bookmark.data.BookmarkDao
import the.autarch.android.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.android.newsgrid.channel.data.ChannelDao
import the.autarch.android.newsgrid.channel.data.ChannelEntity
import the.autarch.android.newsgrid.entry.data.EntryDao
import the.autarch.android.newsgrid.entry.data.EntryEntity

@Database(
    entities = [
        ChannelEntity::class,
        EntryEntity::class,
        BookmarkEntity::class
    ],
    version = 1
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
        .addMigrations()
//        .addMigrations(MIGRATIONS)
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}