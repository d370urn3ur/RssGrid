package the.autarch.newsgrid.entry.data

import com.prof18.rssparser.model.RssItem
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun EntryEntity.Companion.fromRssItem(channelId: String, rssItem: RssItem): EntryEntity? {
    return EntryEntity(
        link = rssItem.link ?: return null,
        channelId = channelId,
        title = rssItem.title ?: return null,
        author = rssItem.author,
        pubDate = rssItem.pubDate,
        timestamp = EntryEntity.parseTimestamp(rssItem.pubDate)?.toEpochMilliseconds(),
        description = rssItem.description,
        content = rssItem.content,
        imageUrl = rssItem.image,
        source = rssItem.sourceUrl
    )
}

@OptIn(ExperimentalTime::class)
fun EntryEntity.Companion.parseTimestamp(pubDate: String?): Instant? {

    val pubDate = pubDate ?: return null

    val timestamp = try {
        Instant.parse(pubDate)
    } catch (t: Throwable) {
        null
    }

    if (timestamp != null) {
        return timestamp
    }

    for (formatter in Entry.formatters) {
        try {
            return formatter.parse(pubDate).toInstantUsingOffset()
        } catch (_: Throwable) {}
    }

    return null
}