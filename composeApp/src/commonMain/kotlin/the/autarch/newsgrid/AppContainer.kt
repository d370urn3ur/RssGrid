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
import the.autarch.newsgrid.channel.presentation.ReorderDirection
import the.autarch.newsgrid.navigation.Route
import the.autarch.newsgrid.navigation.TabIndex
import kotlin.math.max
import kotlin.math.min

@Composable
fun AppContainer(
    tab: TabIndex,
    state: AppContainerState
) {

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
                val idx = channels.indexOf(channel)
                ChannelItem(
                    channel,
                    state.selectedChannels,
                    state.onChannelSelected,
                    state.isReordering,
                    onMove = { direction ->
                        scope.launch {
                            when (direction) {
                                ReorderDirection.UP -> store.moveChannel(fromIndex = idx, toIndex = max(idx - 1, 0))
                                ReorderDirection.DOWN -> store.moveChannel(fromIndex = idx, toIndex = min(idx + 1, channels.size - 1))
                            }
                        }
                    },
                    state.onNavigateToRoute
                )
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
    val isReordering: Boolean,
    val onToggleReorder: () -> Unit,
    val onNavigateToRoute: (Route) -> Unit
)

@Composable
fun rememberAppContainerState(
    selectedChannels: List<ChannelEntity>,
    onChannelSelected: (ChannelEntity) -> Unit,
    isReordering: Boolean,
    onToggleReorder: () -> Unit,
    onNavigateToRoute: (Route) -> Unit
): AppContainerState {

    return remember(
        selectedChannels,
        onChannelSelected,
        isReordering,
        onToggleReorder,
        onNavigateToRoute
    ) {
        AppContainerState(
            selectedChannels = selectedChannels,
            onChannelSelected = onChannelSelected,
            isReordering = isReordering,
            onToggleReorder = onToggleReorder,
            onNavigateToRoute = onNavigateToRoute
        )
    }
}