package the.autarch.android.newsgrid.entry.data

import com.prof18.rssparser.model.RssItem

fun Entry.Companion.fromRssItem(rssItem: RssItem): Entry? {
    return Entry(
        title = rssItem.title ?: return null,
        author = rssItem.author,
        link = rssItem.link ?: return null,
        pubDate = rssItem.pubDate,
        description = rssItem.description,
        content = rssItem.content,
        image = rssItem.image
    )
}

fun Entry.Companion.previewData(): List<Entry> =
    listOf(
        Entry("Title1", "Author1", "link1", null, null, null, null),
        Entry("Title2", "Author1", "link2", null, null, null, null),
        Entry("Title3", "Author1", "link3", null, null, null, null),
        Entry("Title4", "Author1", "link4", null, null, null, null),
    )