package the.autarch.newsgrid.channel.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import the.autarch.newsgrid.LocalDIContainer
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkRepository
import the.autarch.newsgrid.bookmark.presentation.BookmarkDetailsViewModel
import the.autarch.newsgrid.channel.data.ChannelRepository
import the.autarch.newsgrid.channel.usecase.MoveChannelUseCase
import the.autarch.newsgrid.channel.usecase.RefreshChannelsUseCase
import kotlin.math.max
import kotlin.math.min

class ChannelsScreenViewModel(
    channelRepository: ChannelRepository,
    bookmarkRepo: BookmarkRepository,
    val moveChannel: MoveChannelUseCase,
    val refreshChannels: RefreshChannelsUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(ChannelsUiState())
    val uiState = _uiState.asStateFlow()

    private var moveChannelJob: Job? = null
    private var refreshChannelsJob: Job? = null

    init {

        channelRepository.channels
            .onEach { channels ->
                _uiState.update { it.copy(channels = channels) }
            }
            .launchIn(viewModelScope)

        bookmarkRepo.bookmarks
            .onEach { bookmarks ->
                _uiState.update { it.copy(bookmarks = bookmarks) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ChannelsUiAction) {
        when (action) {
            is ChannelsUiAction.MoveChannelUp -> moveChannelUp(action)
            is ChannelsUiAction.MoveChannelDown -> moveChannelDown(action)
            is ChannelsUiAction.RefreshChannels -> refreshChannels(action)
        }
    }

    private fun moveChannelUp(action: ChannelsUiAction.MoveChannelUp) {
        moveChannel(
            fromIndex = action.idx,
            toIndex = max(action.idx - 1, 0)
        )
    }

    private fun moveChannelDown(action: ChannelsUiAction.MoveChannelDown) {
        val channels = uiState.value.channels
        moveChannel(
            fromIndex = action.idx,
            toIndex = min(action.idx + 1, channels.size - 1)
        )
    }

    private fun moveChannel(fromIndex: Int, toIndex: Int) {
        moveChannelJob?.cancel()
        moveChannelJob = moveChannel.execute(fromIndex, toIndex)
            .launchIn(viewModelScope)
    }

    private fun refreshChannels(action: ChannelsUiAction.RefreshChannels) {
        refreshChannelsJob?.cancel()
        refreshChannelsJob = refreshChannels.execute(action.force)
            .onEach { status ->
                when (status) {
                    is TaskProgress.Running -> _uiState.update { it.copy(isRefreshing = true) }
                    else -> _uiState.update { it.copy(isRefreshing = false) }
                }
            }
            .launchIn(viewModelScope)
    }
}

@Composable
fun channelsScreenViewModel(): ChannelsScreenViewModel {
    val container = LocalDIContainer.current
    return viewModel(
        factory = viewModelFactory {
            initializer {
                ChannelsScreenViewModel(
                    container.channelRepository,
                    container.bookmarkRepo,
                    container.createMoveChannelUseCase(),
                    container.createRefreshChannelsUseCase(),
                )
            }
        }
    )
}