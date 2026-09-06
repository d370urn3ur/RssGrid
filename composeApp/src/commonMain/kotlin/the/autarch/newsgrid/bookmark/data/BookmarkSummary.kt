package the.autarch.newsgrid.bookmark.data

data class BookmarkSummary(
    val link: String,
    val title: String,
    val pubDate: String?,
    val timestamp: Long?,
    val author: String?,
    val imageUrl: String?,
    val channelName: String,
    val channelImageUrl: String?
)