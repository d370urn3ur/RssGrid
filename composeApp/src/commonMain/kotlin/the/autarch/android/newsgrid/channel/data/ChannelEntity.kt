package the.autarch.android.newsgrid.channel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "channel",
//    indices = [
//        Index(value = ["link"], unique = true),
//    ]
)
data class ChannelEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id: Long = 0,

    @PrimaryKey
    val link: String,
    val title: String,
    val description: String?,
    val imageUrl: String?
) {
    companion object
}

//factory Feed.fromUniversalFeed(UniversalFeed feed, String canonicalLink, String? faviconUrl) {
//    return Feed(
//        null,
//        feed.title ?? canonicalLink,
//    canonicalLink,
//    faviconUrl ?? feed.icon?.url ?? feed.image?.url,
//    );