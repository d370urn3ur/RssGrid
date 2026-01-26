package the.autarch.newsgrid.channel.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import the.autarch.newsgrid.channel.data.ChannelAndAllEntries
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(
    channels: List<ChannelAndAllEntries>,
    onNavigateToRoute: (Route) -> Unit,
    itemView: @Composable LazyItemScope.(ChannelAndAllEntries) -> Unit,
) {

    val store = LocalChannelStore.current
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {

        if (channels.isEmpty()) {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("You are not subscribed to any channels")

                Button({ onNavigateToRoute(Route.Search) }) {
                    Text("Click here to add or search")
                }
            }

        } else {

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
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(channels) {
                        itemView(it)
                    }
                }
            }
        }
    }
}