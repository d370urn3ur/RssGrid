package the.autarch.newsgrid.channel.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import the.autarch.newsgrid.entry.data.EntryEntity
import the.autarch.newsgrid.entry.data.EntrySummary

@Dao
interface ChannelDao {

    @Query("SELECT * FROM channel ORDER BY listOrder ASC")
    suspend fun getAll(): List<ChannelEntity>

    @Query("SELECT * FROM channel ORDER BY listOrder ASC")
    fun getAllAsFlow(): Flow<List<ChannelEntity>>

    @Transaction
    @Query("SELECT * FROM channel ORDER BY listOrder ASC")
    fun getAllWithEntriesAsFlow(): Flow<List<ChannelAndAllEntries>>

    @Query("UPDATE channel SET listOrder = :listOrder WHERE link = :link")
    suspend fun updateChannelOrder(link: String, listOrder: Int)

    @Transaction
    suspend fun updateChannelsOrder(channels: List<ChannelEntity>) {
        channels.forEachIndexed { index, channel ->
            updateChannelOrder(channel.link, index)
        }
    }

    @Query("SELECT * FROM channel WHERE link = :channelId")
    suspend fun channelForId(channelId: String): ChannelEntity?

    @Insert
    suspend fun insert(item: ChannelEntity): Long

    @Delete
    suspend fun delete(vararg item: ChannelEntity)
}

data class ChannelAndAllEntries(

    @Embedded
    val channel: ChannelEntity? = null,

    @Relation(
        parentColumn = "link",
        entityColumn = "channelId",
        entity = EntryEntity::class
    )
    val entries: List<EntrySummary> = ArrayList()
)