package the.autarch.newsgrid.bookmark.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import the.autarch.newsgrid.entry.data.Entry
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "bookmark"
)
data class BookmarkEntity constructor(
    @PrimaryKey override val link: String,
    override val title: String,
    override val description: String?,
    override val content: String?,
    override val pubDate: String?,
    override val timestamp: Instant?,
//    val updated: String?,
    override val author: String?,
    val imageUrl: String?,
    val source: String?,
    val channelName: String,
    val channelImageUrl: String?
): Entry {
    companion object
}