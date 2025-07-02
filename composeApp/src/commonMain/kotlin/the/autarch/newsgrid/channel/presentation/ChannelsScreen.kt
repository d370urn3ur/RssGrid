package the.autarch.newsgrid.channel.presentation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import the.autarch.newsgrid.channel.data.ChannelAndAllEntries
import the.autarch.newsgrid.channel.data.LocalChannelStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(channels: List<ChannelAndAllEntries>, itemView: @Composable LazyItemScope.(ChannelAndAllEntries) -> Unit) {

    val store = LocalChannelStore.current
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing,
        onRefresh = {
            isRefreshing = true
            scope.launch {
                store.refreshChannels(true)
                isRefreshing = false
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(channels) {
                itemView(it)
            }
        }
    }
}