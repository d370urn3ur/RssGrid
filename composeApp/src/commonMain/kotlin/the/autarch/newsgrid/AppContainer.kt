package the.autarch.newsgrid

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import the.autarch.newsgrid.bookmark.data.BookmarkSummary
import the.autarch.newsgrid.bookmark.presentation.BookmarksScreen
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.presentation.ChannelsScreen
import the.autarch.newsgrid.navigation.Route
import the.autarch.newsgrid.navigation.TabIndex

@Composable
fun AppContainer(
    tab: TabIndex,
    state: AppContainerState,
    bookmarks: List<BookmarkSummary>,
    onAppAction: (AppUiAction) -> Unit,
) {

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        onAppAction(AppUiAction.RefreshChannels)
    }

    Column {

        when (tab) {

            TabIndex.CHANNELS -> ChannelsScreen(
                state = state,
                onNavigateToRoute = state.onNavigateToRoute
            )

            TabIndex.BOOKMARKS -> BookmarksScreen(bookmarks) { route ->
                state.onNavigateToRoute(route)
            }
        }
    }
}

class AppContainerState(
    val selectedChannels: List<ChannelEntity>,
    val isReordering: Boolean,
    val onAppAction: (AppUiAction) -> Unit,
    val onNavigateToRoute: (Route) -> Unit
)

@Composable
fun rememberAppContainerState(
    selectedChannels: List<ChannelEntity>,
    isReordering: Boolean,
    onAppAction: (AppUiAction) -> Unit,
    onNavigateToRoute: (Route) -> Unit
): AppContainerState {

    return remember(
        selectedChannels,
        isReordering,
        onAppAction,
        onNavigateToRoute
    ) {
        AppContainerState(
            selectedChannels = selectedChannels,
            isReordering = isReordering,
            onAppAction = onAppAction,
            onNavigateToRoute = onNavigateToRoute
        )
    }
}