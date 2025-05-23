package the.autarch.newsgrid.channel.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import the.autarch.newsgrid.channel.data.ChannelAndAllEntries

@Composable
fun ChannelsScreen(channels: List<ChannelAndAllEntries>, itemView: @Composable LazyItemScope.(ChannelAndAllEntries) -> Unit) {
    LazyColumn {
        items(channels) {
            itemView(it)
        }
    }
}