package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.launchIn
import the.autarch.newsgrid.LocalDIContainer
import the.autarch.newsgrid.bookmark.usecase.RemoveBookmarkUseCase

class BookmarksViewModel(
    private val removeBookmark: RemoveBookmarkUseCase
): ViewModel() {

//    private val _uiState = MutableStateFlow(BookmarksUiState())
//    val uiState = _uiState.asStateFlow()

    fun onAction(action: BookmarksUiAction) {
        when (action) {
            is BookmarksUiAction.RemoveBookmark -> {
                removeBookmark(action.bookmarkId)
            }
        }
    }

    private fun removeBookmark(id: String) {
        removeBookmark.execute(id)
//            .onEach { status ->
//                _uiState.update { it.copy(removeBookmarkOperation = status) }
//            }
            .launchIn(viewModelScope)
    }
}

@Composable
fun bookmarksViewModel(): BookmarksViewModel {
    val container = LocalDIContainer.current
    return viewModel(
        factory = viewModelFactory {
            initializer {
                BookmarksViewModel(container.createRemoveBookmarkUseCase())
            }
        }
    )
}