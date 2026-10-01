package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import the.autarch.newsgrid.bookmark.data.BookmarkSummary
import the.autarch.newsgrid.navigation.Route

@Composable
fun BookmarksScreen(
    bookmarks: List<BookmarkSummary>,
    viewModel: BookmarksViewModel = bookmarksViewModel(),
    onNavigateToRoute: (Route) -> Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(bookmarks, key = { it.link }) { bookmark ->
            BookmarkItem(
                bookmark,
                Modifier.animateItem()
                    .clickable {
                        onNavigateToRoute(Route.BookmarkDetails(bookmark.link, bookmark.channelName))
                    },
                onRemoveBoomark = { viewModel.onAction(BookmarksUiAction.RemoveBookmark(bookmark.link)) }
            )
        }
    }
}

//@Stable
//data class BookmarksUiState(
//    val removeBookmarkOperation: TaskProgress<Unit> = TaskProgress.Idle
//)

sealed interface BookmarksUiAction {
    data class RemoveBookmark(val bookmarkId: String): BookmarksUiAction
}