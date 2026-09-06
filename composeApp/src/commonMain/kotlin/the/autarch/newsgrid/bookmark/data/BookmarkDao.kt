package the.autarch.newsgrid.bookmark.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark")
    fun getAllAsFlow(): Flow<List<BookmarkSummary>>

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE) // REPLACE ?
    suspend fun insert(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmark WHERE link = :bookmarkId")
    suspend fun bookmarkForId(bookmarkId: String): BookmarkEntity?

    @Delete
    suspend fun delete(vararg item: BookmarkEntity)
}