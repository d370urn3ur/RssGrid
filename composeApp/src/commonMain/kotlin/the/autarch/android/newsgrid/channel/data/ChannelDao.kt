package the.autarch.android.newsgrid.channel.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import the.autarch.android.newsgrid.entry.data.EntryEntity

@Dao
interface ChannelDao {

//    @Query("SELECT count(*) FROM channel")
//    suspend fun count(): Int

    @Query("SELECT * FROM channel")
    fun getAllAsFlow(): Flow<List<ChannelEntity>>

    @Transaction
    @Query("SELECT * FROM channel")
    fun getAllWithEntriesAsFlow(): Flow<List<ChannelAndAllEntries>>

    @Query("SELECT * FROM channel WHERE link = :channelId")
    suspend fun channelForId(channelId: String): ChannelEntity?

    @Insert
    suspend fun insert(item: ChannelEntity): Long
}

data class ChannelAndAllEntries(
    @Embedded
    val channel: ChannelEntity? = null,

    @Relation(
        parentColumn = "link",
        entityColumn = "channelId"
    )
    val entries: List<EntryEntity> = ArrayList()
)