package the.autarch.newsgrid.channel.data

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.diamondedge.logging.KmLogging
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import io.ktor.http.URLBuilder
import io.ktor.http.set
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until
import the.autarch.newsgrid.AppDatabase
import the.autarch.newsgrid.LAST_UPDATE
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.entry.data.EntryEntity
import the.autarch.newsgrid.entry.data.fromRssItem
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ChannelStore(private val appDatabase: AppDatabase, private val parser: RssParser, private val prefs: DataStore<Preferences>) {

    companion object {
        const val REFRESH_THRESHOLD = 10
    }

    val channels = appDatabase.getChannelDao().getAllWithEntriesAsFlow()
    val bookmarks = appDatabase.getBookmarkDao().getAllAsFlow()

    suspend fun refreshChannels(force: Boolean = false) {
        if (!force) {
            val lastUpdateMillis = prefs.data.map {
                it[LAST_UPDATE] ?: Instant.DISTANT_PAST.toEpochMilliseconds()
            }.first()
            val lastUpdate = Instant.fromEpochMilliseconds(lastUpdateMillis)
            val diffMinutes = lastUpdate.until(Clock.System.now(), DateTimeUnit.MINUTE, TimeZone.UTC)
            if (diffMinutes < REFRESH_THRESHOLD) return
        }

        appDatabase.getChannelDao().getAll().forEach { channel ->
            updateChannel(channel)
        }

        prefs.edit { settings ->
            settings[LAST_UPDATE] = Clock.System.now().toEpochMilliseconds()
        }
    }

    suspend fun updateChannel(channel: ChannelEntity) {
        KmLogging.info("RSS", "updating channels")
        try {
            val rssChannel = fetchChannelRss(channel.link)
            updateEntries(channel, rssChannel)
        } catch (t: Throwable) {
            KmLogging.error("RSS", "updateChannel: ${channel.link}", t)
        }
    }

    suspend fun addChannel(channelUrl: String) {
        val rssChannel = fetchChannelRss(channelUrl)
        var favicon = rssChannel.image?.url
        if (favicon == null) {
            favicon = fetchFavicon(rssChannel)
        }
        val channel = ChannelEntity.fromRssChannel(channelUrl, rssChannel, favicon)
        appDatabase.getChannelDao().insert(channel)
        updateEntries(channel, rssChannel)
    }

    suspend fun deleteChannels(channels: List<ChannelEntity>) {
        appDatabase.getChannelDao().delete(*channels.toTypedArray())
    }

    private suspend fun fetchFavicon(rssChannel: RssChannel): String? {

        val sourceUrl = rssChannel.link ?: return null
        val toShorten = URLBuilder(sourceUrl).build()
        val shortened = URLBuilder(protocol = toShorten.protocol, host = toShorten.host).build()

        try {

            val doc: Document = Ksoup.parseGetRequest(shortened.toString())
            val iconLinks = doc.select("link[rel*=icon]")
            if (iconLinks.isNotEmpty()) {
                val favicon = iconLinks.firstOrNull()?.attribute("href")?.value
                if (favicon != null) {
                    return if (favicon.startsWith("http")) {
                        favicon
                    } else {
                        URLBuilder(shortened.toString()).apply {
                            set(path = favicon)
                        }.build().toString()
                    }
                }
            }
            return null

        } catch (t: Throwable) {
            KmLogging.error("FAVICON", "Error parsing HTML:", t)
            return null
        }
    }

    private suspend fun fetchChannelRss(channelUrl: String): RssChannel =
        parser.getRssChannel(channelUrl)

    private suspend fun updateEntries(channel: ChannelEntity, rssChannel: RssChannel) {
        val entries = rssChannel.items.toTypedArray()
            .mapNotNull { item -> EntryEntity.fromRssItem(channel.link, item) }
            .toTypedArray()
        appDatabase.getEntryDao().insertAndCleanup(channel.link, entries)
    }

    suspend fun getEntry(entryId: String): EntryEntity? =
        appDatabase.getEntryDao().entryForId(entryId)

    suspend fun getBookmark(bookmarkId: String): BookmarkEntity? =
        appDatabase.getBookmarkDao().bookmarkForId(bookmarkId)

    suspend fun saveBookmark(entryId: String) {
        val entry = appDatabase.getEntryDao().entryForId(entryId)
        entry?.let {
            appDatabase.getChannelDao().channelForId(entry.channelId)?.let { channel ->
                val bookmark = BookmarkEntity(
                    link = entry.link,
                    title = entry.title,
                    description = entry.description,
                    content = entry.content,
                    pubDate = entry.pubDate,
                    timestamp = entry.timestamp,
                    author = entry.author,
                    imageUrl = entry.imageUrl,
                    source = entry.source,
                    channelName = channel.title,
                    channelImageUrl = channel.imageUrl
                )
                appDatabase.getBookmarkDao().insert(bookmark)
            }
        }
    }

    suspend fun removeBookmark(entryId: String) {
        appDatabase.getBookmarkDao().bookmarkForId(entryId)?.let {
            appDatabase.getBookmarkDao().delete(it)
        }
    }
}

val LocalChannelStore = staticCompositionLocalOf<ChannelStore> {
    error("No CompositionLocal LocalChannelStore")
}
