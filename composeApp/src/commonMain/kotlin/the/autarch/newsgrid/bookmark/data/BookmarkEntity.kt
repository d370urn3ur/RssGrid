package the.autarch.newsgrid.bookmark.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmark"
)
data class BookmarkEntity(
    @PrimaryKey val link: String,
    val title: String,
    val description: String?,
    val content: String?,
    val published: String?,
//    val updated: String?,
    val author: String?,
    val imageUrl: String?,
    val source: String?,
    val channelName: String,
    val channelImageUrl: String?
) {
    companion object
}