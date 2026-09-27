package the.autarch.newsgrid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import the.autarch.newsgrid.bookmark.presentation.BookmarkItem
import the.autarch.newsgrid.bookmark.presentation.BookmarksScreen
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.channel.presentation.ChannelItem
import the.autarch.newsgrid.channel.presentation.ChannelsScreen
import the.autarch.newsgrid.navigation.Route
import the.autarch.newsgrid.navigation.TabIndex

@Composable
fun AppContainer(tab: TabIndex, state: AppContainerState) {

    val scope = rememberCoroutineScope()
    val store = LocalChannelStore.current
    val channels by LocalChannelStore.current.channels.collectAsStateWithLifecycle(emptyList())
    val bookmarks by LocalChannelStore.current.bookmarks.collectAsStateWithLifecycle(emptyList())

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        scope.launch {
            store.refreshChannels()
        }
    }

    Column {

        when (tab) {

            TabIndex.CHANNELS -> ChannelsScreen(
                channels,
                onNavigateToRoute = state.onNavigateToRoute
            ) { channel ->
                ChannelItem(channel, state.selectedChannels, state.onChannelSelected, state.onNavigateToRoute)
            }

            TabIndex.BOOKMARKS -> BookmarksScreen(bookmarks) { bookmark ->
                BookmarkItem(
                    bookmark,
                    Modifier.animateItem()
                        .clickable {
                            state.onNavigateToRoute(Route.BookmarkDetails(bookmark.link, bookmark.channelName))
                        }
                )
            }
        }
    }
}

class AppContainerState(
    val selectedChannels: List<ChannelEntity>,
    val onChannelSelected: (ChannelEntity) -> Unit,
    val onNavigateToRoute: (Route) -> Unit
)

@Composable
fun rememberAppContainerState(
    selectedChannels: List<ChannelEntity>,
    onChannelSelected: (ChannelEntity) -> Unit,
    onNavigateToRoute: (Route) -> Unit
): AppContainerState {

    return remember(selectedChannels, onChannelSelected, onNavigateToRoute) {
        AppContainerState(
            selectedChannels = selectedChannels,
            onChannelSelected = onChannelSelected,
            onNavigateToRoute = onNavigateToRoute
        )
    }
}