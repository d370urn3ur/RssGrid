package the.autarch.newsgrid.channel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "channel",
)
data class ChannelEntity(
    @PrimaryKey
    val link: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val listOrder: Int = 0
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