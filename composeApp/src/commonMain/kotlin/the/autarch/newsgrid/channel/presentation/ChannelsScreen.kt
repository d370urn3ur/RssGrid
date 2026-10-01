package the.autarch.newsgrid.channel.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import the.autarch.newsgrid.AppContainerState
import the.autarch.newsgrid.AppUiAction
import the.autarch.newsgrid.bookmark.data.BookmarkSummary
import the.autarch.newsgrid.channel.data.ChannelAndAllEntries
import the.autarch.newsgrid.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(
    state: AppContainerState,
    viewModel: ChannelsScreenViewModel = channelsScreenViewModel(),
    onNavigateToRoute: (Route) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxSize()) {

        if (uiState.channels.isEmpty()) {

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
                uiState.isRefreshing,
                onRefresh = {
                    viewModel.onAction(ChannelsUiAction.RefreshChannels(true))
                }
            ) {
                LazyColumn(
                    Modifier.fillMaxWidth()
                ) {
                    items(uiState.channels) { channel ->
                        val idx = uiState.channels.indexOf(channel)
                        ChannelItem(
                            channel,
                            state.selectedChannels,
                            { state.onAppAction(AppUiAction.ToggleChannelSelected(it)) },
                            uiState.bookmarks,
                            state.isReordering,
                            onMove = { direction ->
                                when (direction) {
                                    ReorderDirection.UP -> viewModel.onAction(
                                        ChannelsUiAction.MoveChannelUp(idx)
                                    )
                                    ReorderDirection.DOWN -> viewModel.onAction(
                                        ChannelsUiAction.MoveChannelDown(idx)
                                    )
                                }
                            },
                            state.onNavigateToRoute
                        )
                    }
                }
            }
        }
    }
}

@Stable
data class ChannelsUiState(
    val channels: List<ChannelAndAllEntries> = emptyList(),
    val bookmarks: List<BookmarkSummary> = emptyList(),
    val isRefreshing: Boolean = false
)

sealed interface ChannelsUiAction {
    data class MoveChannelUp(var idx: Int): ChannelsUiAction
    data class MoveChannelDown(var idx: Int): ChannelsUiAction
    data class RefreshChannels(var force: Boolean): ChannelsUiAction
}