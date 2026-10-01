package the.autarch.newsgrid.entry.presentation

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
import the.autarch.newsgrid.bookmark.presentation.BookmarksViewModel
import the.autarch.newsgrid.entry.usecase.GetEntryUseCase

class EntryDetailsViewModel(
    val getEntry: GetEntryUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(EntryDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private var getEntryJob: Job? = null

    fun getEntry(entryId: String) {
        getEntryJob?.cancel()
        getEntryJob = getEntry.execute(entryId)
            .onEach { status ->
                _uiState.update { it.copy(getEntryOperation = status) }
            }
            .launchIn(viewModelScope)
    }
}

@Composable
fun entryDetailsViewModel(): EntryDetailsViewModel {
    val container = LocalDIContainer.current
    return viewModel(
        factory = viewModelFactory {
            initializer {
                EntryDetailsViewModel(container.createGetEntryUseCase())
            }
        }
    )
}