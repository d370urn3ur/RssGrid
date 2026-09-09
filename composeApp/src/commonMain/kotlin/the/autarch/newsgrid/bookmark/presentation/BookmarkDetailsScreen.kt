package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.entry.presentation.EntryDetailsScreenContent

@Composable
fun BookmarkDetailsScreen(bookmarkId: String) {

    val scope = rememberCoroutineScope()
    val store = LocalChannelStore.current
    var bookmark by remember { mutableStateOf<BookmarkEntity?>(null) }

    LaunchedEffect(bookmarkId) {
        scope.launch {
            bookmark = store.getBookmark(bookmarkId)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        bookmark?.let { entry ->
            EntryDetailsScreenContent(entry)
        }
    }
}