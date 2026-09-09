package the.autarch.newsgrid.entry.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry WHERE link = :entryId")
    suspend fun entryForId(entryId: String): EntryEntity?

    @Transaction
    suspend fun insertAndCleanup(channelId: String, entries: Array<EntryEntity>) {
        insert(*entries)
        deleteOldEntries(channelId)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE) // REPLACE ?
    suspend fun insert(vararg item: EntryEntity)

    @Delete
    suspend fun delete(vararg item: EntryEntity)

    @Query("""
        DELETE FROM entry
        WHERE channelId = :channelId
        AND link NOT IN (
            SELECT link FROM entry
            WHERE channelId = :channelId
            ORDER BY timestamp DESC, link DESC
            LIMIT 40
        )
    """)
    suspend fun deleteOldEntries(channelId: String)
}