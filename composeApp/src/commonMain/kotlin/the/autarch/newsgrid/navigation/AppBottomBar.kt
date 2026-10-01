package the.autarch.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import the.autarch.newsgrid.AppUiAction

@Composable
fun AppBottomBar(
    currentlyVisibleRoute: NavKey,
    bookmarkIds: List<String>,
    onAppAction: (AppUiAction) -> Unit
) {

    when (currentlyVisibleRoute) {

        is Route.EntryDetails -> {
            val isBookmarked = bookmarkIds.contains(currentlyVisibleRoute.entryId)
            EntryDetailBottomBar(
                currentlyVisibleRoute.entryId,
                isBookmarked,
                onBookmarkClicked = {
                    if (isBookmarked) {
                        onAppAction(AppUiAction.RemoveBookmark(currentlyVisibleRoute.entryId))
                    } else {
                        onAppAction(AppUiAction.AddBookmark(currentlyVisibleRoute.entryId))
                    }
                }
            )
        }

        is Route.BookmarkDetails -> {
            val isBookmarked = bookmarkIds.contains(currentlyVisibleRoute.bookmarkId)
            EntryDetailBottomBar(
                currentlyVisibleRoute.bookmarkId,
                isBookmarked,
                onBookmarkClicked = {
                    if (isBookmarked) {
                        onAppAction(AppUiAction.RemoveBookmark(currentlyVisibleRoute.bookmarkId))
                    } else {
                        onAppAction(AppUiAction.AddBookmark(currentlyVisibleRoute.bookmarkId))
                    }
                }
            )
        }
    }
}