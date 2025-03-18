package the.autarch.android.newsgrid.channel.data

import com.prof18.rssparser.model.RssChannel
import the.autarch.android.newsgrid.entry.data.Entry
import the.autarch.android.newsgrid.entry.data.fromRssItem
import the.autarch.android.newsgrid.entry.data.previewData

fun Channel.Companion.fromRssChannel(rssChannel: RssChannel): Channel {
    return Channel(
        title = rssChannel.title,
        link = rssChannel.link,
        description = rssChannel.description,
        imageUrl = rssChannel.image?.url,
        items = rssChannel.items.mapNotNull {
            Entry.fromRssItem(it)
        }
    )
}

fun Channel.Companion.previewData(): List<Channel> =
    listOf(
        Channel(
            "Ars Technica",
            "arstechnia.com",
            "",
            "https://cdn.arstechnica.net/wp-content/uploads/2016/10/cropped-ars-logo-512_480-60x60.png",
            Entry.previewData()
        )
    )