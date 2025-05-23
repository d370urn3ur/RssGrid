package the.autarch.newsgrid.entry.data

import com.prof18.rssparser.model.RssItem

fun EntryEntity.Companion.fromRssItem(channelId: String, rssItem: RssItem): EntryEntity? {
    return EntryEntity(
        link = rssItem.link ?: return null,
        channelId = channelId,
        title = rssItem.title ?: return null,
        author = rssItem.author,
        published = rssItem.pubDate,
        description = rssItem.description,
        content = rssItem.content,
        imageUrl = rssItem.image,
        source = rssItem.sourceUrl
    )
}