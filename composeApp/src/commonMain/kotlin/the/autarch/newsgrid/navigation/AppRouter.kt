package the.autarch.newsgrid.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import the.autarch.newsgrid.AppContainer
import the.autarch.newsgrid.AppUiAction
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkSummary
import the.autarch.newsgrid.bookmark.presentation.BookmarkDetailsScreen
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.entry.presentation.EntryDetailsScreen
import the.autarch.newsgrid.rememberAppContainerState
import the.autarch.newsgrid.search.presentation.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRouter(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    selectedChannels: List<ChannelEntity>,
    isReordering: Boolean,
    addChannelOperation: TaskProgress<String>,
    onAppAction: (AppUiAction) -> Unit,
    bookmarks: List<BookmarkSummary>
) {

    val containerState = rememberAppContainerState(
        selectedChannels = selectedChannels,
        isReordering = isReordering,
        onAppAction = onAppAction,
        onNavigateToRoute = { route -> backStack.add(route) }
    )

    val currentRoute = backStack.last()

    Column(modifier = modifier) {
        if (currentRoute is Route.Main) {
            PrimaryTabRow(selectedTabIndex = currentRoute.tab.idxVal) {
                TabIndex.entries.forEach { tab ->
                    Tab(
                        selected = currentRoute.tab == tab,
                        onClick = {
                            if (currentRoute.tab != tab) {
                                if (tab == TabIndex.CHANNELS) {
                                    backStack.removeLast()
                                } else {
                                    backStack.add(Route.Main(tab))
                                }
                            }
                        },
                        text = { Text(text = tab.title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }
        }

        NavDisplay(
            backStack = backStack,
            modifier = Modifier.weight(1f),
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Route.Main> { key ->
                    AppContainer(
                        key.tab,
                        containerState,
                        bookmarks,
                        onAppAction = onAppAction
                    )
                }
                entry<Route.Search> {
                    SearchScreen(
                        addChannelOperation = addChannelOperation,
                        onAddChannel = { onAppAction(AppUiAction.AddChannel(it)) }
                    )
                }
                entry<Route.EntryDetails> { key ->
                    EntryDetailsScreen(key.entryId)
                }
                entry<Route.BookmarkDetails> { key ->
                    BookmarkDetailsScreen(key.bookmarkId)
                }
            },
            transitionSpec = {
                // Forward nav
                slideInHorizontally(initialOffsetX = { it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it })
            },
            popTransitionSpec = {
                // Backward nav
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            predictivePopTransitionSpec = {
                // back gesture
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            }
        )
    }
}
