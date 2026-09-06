package the.autarch.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey

@Composable
fun AppBottomBar(currentlyVisibleRoute: NavKey, bookmarkIds: List<String>) {

    when (currentlyVisibleRoute) {

        is Route.EntryDetails -> {
            val isBookmarked = bookmarkIds.contains(currentlyVisibleRoute.entryId)
            EntryDetailBottomBar(currentlyVisibleRoute.entryId, isBookmarked)
        }

        is Route.BookmarkDetails -> {
            val isBookmarked = bookmarkIds.contains(currentlyVisibleRoute.bookmarkId)
            EntryDetailBottomBar(currentlyVisibleRoute.bookmarkId, isBookmarked)
        }
    }
}