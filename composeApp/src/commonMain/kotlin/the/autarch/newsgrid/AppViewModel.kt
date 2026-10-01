package the.autarch.newsgrid

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkRepository
import the.autarch.newsgrid.bookmark.usecase.AddBookmarkUseCase
import the.autarch.newsgrid.bookmark.usecase.RemoveBookmarkUseCase
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.usecase.AddChannelUseCase
import the.autarch.newsgrid.channel.usecase.DeleteChannelsUseCase
import the.autarch.newsgrid.channel.usecase.RefreshChannelsUseCase

class AppViewModel(
    bookmarkRepo: BookmarkRepository,
    private val addChannel: AddChannelUseCase,
    private val deleteChannels: DeleteChannelsUseCase,
    private val refreshChannels: RefreshChannelsUseCase,
    private val addBookmark: AddBookmarkUseCase,
    private val removeBookmark: RemoveBookmarkUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        bookmarkRepo.bookmarks
            .onEach { bookmarks ->
                _uiState.update { it.copy(bookmarks = bookmarks) }
            }
            .launchIn(viewModelScope)
    }

    private var addChannelJob: Job? = null
    private var deleteChannelsJob: Job? = null
    private var addBookmarkJob: Job? = null
    private var removeBookmarkJob: Job? = null
    private var refreshChannelsJob: Job? = null

    fun onAction(action: AppUiAction) {
        when (action) {
            is AppUiAction.AddChannel -> addChannel(action.url)
            is AppUiAction.DeleteChannels -> deleteChannels(action.channels)
            is AppUiAction.OnAddChannelHandled -> {
                _uiState.update { it.copy(addChannelOperation = TaskProgress.Idle) }
            }
            is AppUiAction.RefreshChannels -> refreshChannels()
            is AppUiAction.ToggleChannelSelected -> toggleChannelSelected(action)
            is AppUiAction.DeselectChannels -> {
                _uiState.update { it.copy(selectedChannels = emptyList()) }
            }
            is AppUiAction.AddBookmark -> addBookmark(action.entryId)
            is AppUiAction.RemoveBookmark -> removeBookmark(action.entryId)
            is AppUiAction.ToggleReorderingMode -> _uiState.update { it.copy(isReordering = !it.isReordering) }
        }
    }

    private fun addChannel(url: String) {
        addChannelJob?.cancel()
        addChannelJob = addChannel.execute(url)
            .onEach { status ->
                _uiState.update { it.copy(addChannelOperation = status) }
            }
            .launchIn(viewModelScope)
    }

    private fun deleteChannels(channels: List<ChannelEntity>) {
        deleteChannelsJob?.cancel()
        deleteChannelsJob = deleteChannels.execute(channels)
            .launchIn(viewModelScope)
    }

    private fun addBookmark(id: String) {
        addBookmarkJob?.cancel()
        addBookmarkJob = addBookmark.execute(id)
            .launchIn(viewModelScope)
    }

    private fun removeBookmark(id: String) {
        removeBookmarkJob?.cancel()
        removeBookmarkJob = removeBookmark.execute(id)
            .launchIn(viewModelScope)
    }

    private fun refreshChannels() {
        refreshChannelsJob?.cancel()
        refreshChannelsJob = refreshChannels.execute()
            .launchIn(viewModelScope)
    }

    private fun toggleChannelSelected(action: AppUiAction.ToggleChannelSelected) {
        val selectedChannels = uiState.value.selectedChannels.toMutableList()
        if (selectedChannels.contains(action.channel)) {
            selectedChannels -= action.channel
        } else {
            selectedChannels += action.channel
        }
        _uiState.update { it.copy(selectedChannels = selectedChannels.toList()) }
    }
}

@Composable
fun appViewModel(container: DIContainer): AppViewModel {
    return viewModel(
        factory = viewModelFactory {
            initializer {
                AppViewModel(
                    container.bookmarkRepo,
                    container.createAddChannelUseCase(),
                    container.createDeleteChannelUseCase(),
                    container.createRefreshChannelsUseCase(),
                    container.createAddBookmarkUseCase(),
                    container.createRemoveBookmarkUseCase()
                )
            }
        }
    )
}