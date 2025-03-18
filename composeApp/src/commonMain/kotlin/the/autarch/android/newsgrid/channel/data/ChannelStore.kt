package the.autarch.android.newsgrid.channel.data

import androidx.compose.runtime.staticCompositionLocalOf
import com.prof18.rssparser.RssParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChannelStore(private val parser: RssParser) {

    private val _channels: MutableStateFlow<List<Channel>> = MutableStateFlow(emptyList())
    val channels = _channels.asStateFlow()

    suspend fun addChannel(channelUrl: String) {
        val rssChannel = parser.getRssChannel(channelUrl)
        val channel = Channel.fromRssChannel(rssChannel)
        val current = _channels.value.toMutableList()
        current.add(channel)
        _channels.value = current
    }
}

val LocalChannelStore = staticCompositionLocalOf<ChannelStore> {
    error("No CompositionLocal LocalChannelStore")
}
