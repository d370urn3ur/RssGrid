package the.autarch.newsgrid.channel.data

import com.prof18.rssparser.model.RssChannel

fun ChannelEntity.Companion.fromRssChannel(channelUrl: String, rssChannel: RssChannel, favicon: String?): ChannelEntity {
    return ChannelEntity(
        title = rssChannel.title ?: channelUrl,
        link = channelUrl,
        description = rssChannel.description,
        imageUrl = favicon,
    )
}