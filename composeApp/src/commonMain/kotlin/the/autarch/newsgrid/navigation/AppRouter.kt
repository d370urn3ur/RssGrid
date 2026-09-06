package the.autarch.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import the.autarch.newsgrid.AppContainer
import the.autarch.newsgrid.bookmark.presentation.BookmarkDetailsScreen
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.entry.presentation.EntryDetailsScreen
import the.autarch.newsgrid.rememberAppContainerState
import the.autarch.newsgrid.search.presentation.SearchScreen

@Composable
fun AppRouter(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    selectedChannels: List<ChannelEntity>,
    onSelectChannel: (ChannelEntity) -> Unit
) {

    val containerState = rememberAppContainerState(
        selectedChannels = selectedChannels,
        onChannelSelected = onSelectChannel,
        onNavigateToRoute = { route -> backStack.add(route) }
    )

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Route.Main> { key ->
                AppContainer(containerState)
            }
            entry<Route.Search> { key ->
                SearchScreen()
            }
            entry<Route.EntryDetails> { key ->
                EntryDetailsScreen(key.entryId)
            }
            entry<Route.BookmarkDetails> { key ->
                BookmarkDetailsScreen(key.bookmarkId)
            }
        }
    )
}