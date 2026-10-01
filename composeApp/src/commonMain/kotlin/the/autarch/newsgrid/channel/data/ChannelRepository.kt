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
import the.autarch.newsgrid.entry.data.EntryEntity
import the.autarch.newsgrid.entry.data.fromRssItem
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ChannelRepository(
    private val appDatabase: AppDatabase,
    private val parser: RssParser,
) {

    val channels = appDatabase.getChannelDao().getAllWithEntriesAsFlow()

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

    suspend fun refreshChannels() {
        appDatabase.getChannelDao().getAll().forEach { channel ->
            updateChannel(channel)
        }
    }

    suspend fun moveChannel(fromIndex: Int, toIndex: Int) {
        val mutableChannels = appDatabase.getChannelDao().getAll().toMutableList()
        val item = mutableChannels.removeAt(fromIndex)
        mutableChannels.add(toIndex, item)
        updateChannelsOrder(mutableChannels)
    }

    suspend fun getEntry(entryId: String): EntryEntity? =
        appDatabase.getEntryDao().entryForId(entryId)

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

    private suspend fun updateChannel(channel: ChannelEntity) {
        KmLogging.info("RSS", "updating channels")
        try {
            val rssChannel = fetchChannelRss(channel.link)
            updateEntries(channel, rssChannel)
        } catch (t: Throwable) {
            KmLogging.error("RSS", "updateChannel: ${channel.link}", t)
        }
    }

    private suspend fun updateChannelsOrder(reorderedChannels: List<ChannelEntity>) {
        appDatabase.getChannelDao().updateChannelsOrder(reorderedChannels)
    }

    private suspend fun fetchChannelRss(channelUrl: String): RssChannel =
        parser.getRssChannel(channelUrl)

    private suspend fun updateEntries(channel: ChannelEntity, rssChannel: RssChannel) {
        val entries = rssChannel.items.toTypedArray()
            .mapNotNull { item -> EntryEntity.fromRssItem(channel.link, item) }
            .toTypedArray()
        appDatabase.getEntryDao().insertAndCleanup(channel.link, entries)
    }
}
