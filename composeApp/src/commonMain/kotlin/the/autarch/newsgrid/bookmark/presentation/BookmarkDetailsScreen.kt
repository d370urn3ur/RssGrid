package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.entry.presentation.EntryDetailsScreenContent

@Composable
fun BookmarkDetailsScreen(bookmarkId: String) {

    val scope = rememberCoroutineScope()
    val store = LocalChannelStore.current
    var entry by remember { mutableStateOf<BookmarkEntity?>(null) }

    LaunchedEffect(bookmarkId) {
        scope.launch {
            entry = store.getBookmark(bookmarkId)
        }
    }

    entry?.let { entry ->
        EntryDetailsScreenContent(entry)
    }
}