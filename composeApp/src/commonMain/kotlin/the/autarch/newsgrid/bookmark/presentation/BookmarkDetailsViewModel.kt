package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import the.autarch.newsgrid.LocalDIContainer
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.bookmark.usecase.GetBookmarkUseCase

class BookmarkDetailsViewModel(
    private val getBookmark: GetBookmarkUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(BookmarkDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: BookmarkDetailsUiAction) {
        when (action) {
            is BookmarkDetailsUiAction.GetBookmark -> {
                getBookmark(action.bookmarkId)
            }
        }
    }

    private fun getBookmark(id: String) {
        getBookmark.execute(id)
            .onEach { status ->
                _uiState.update { it.copy(getBookmarkOperation = status) }
            }
            .launchIn(viewModelScope)
    }
}

@Composable
fun bookmarkDetailsViewModel(): BookmarkDetailsViewModel {
    val container = LocalDIContainer.current
    return viewModel(
        factory = viewModelFactory {
            initializer {
                BookmarkDetailsViewModel(container.createGetBookmarkUseCase())
            }
        }
    )
}