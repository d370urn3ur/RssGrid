package the.autarch.newsgrid.entry.data

data class EntrySummary(
    val link: String,
    val channelId: String,
    val title: String,
    val pubDate: String?,
    val timestamp: Long?,
    val author: String?,
    val imageUrl: String?
)