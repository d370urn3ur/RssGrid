package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.bookmark.data.BookmarkSummary

@Composable
fun BookmarksScreen(
    bookmarks: List<BookmarkSummary>,
    item: @Composable LazyItemScope.(BookmarkSummary) -> Unit
) {

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(bookmarks, key = { it.link }) {
            item(it)
        }
    }
}

