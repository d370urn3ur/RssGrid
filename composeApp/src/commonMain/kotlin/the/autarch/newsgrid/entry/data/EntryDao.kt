package the.autarch.newsgrid.entry.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry WHERE link = :entryId")
    suspend fun entryForId(entryId: String): EntryEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE) // REPLACE ?
    suspend fun insert(vararg item: EntryEntity)

    @Delete
    suspend fun delete(vararg item: EntryEntity)
}