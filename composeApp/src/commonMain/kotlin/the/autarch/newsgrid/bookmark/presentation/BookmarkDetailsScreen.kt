package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.entry.presentation.EntryDetailsScreenContent

@Composable
fun BookmarkDetailsScreen(
    bookmarkId: String,
    viewModel: BookmarkDetailsViewModel = bookmarkDetailsViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(bookmarkId) {
        viewModel.onAction(BookmarkDetailsUiAction.GetBookmark(bookmarkId))
    }

    when (val status = uiState.getBookmarkOperation) {
        is TaskProgress.Success -> Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EntryDetailsScreenContent(status.result)
        }
        else -> {
            // TODO: show loading??
        }
    }
}

@Stable
data class BookmarkDetailsUiState(
    val getBookmarkOperation: TaskProgress<BookmarkEntity> = TaskProgress.Idle
)

sealed interface BookmarkDetailsUiAction {
    data class GetBookmark(val bookmarkId: String): BookmarkDetailsUiAction
}