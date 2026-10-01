package the.autarch.newsgrid.search.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
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
import the.autarch.newsgrid.LocalDIContainer
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.search.data.SearchResult
import the.autarch.newsgrid.search.usecase.SearchUseCase

class SearchScreenViewModel(
    val searchUseCase: SearchUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUiState())
    val uiState: StateFlow<SearchScreenUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onAction(action: SearchScreenUiAction) {
        when (action) {
            is SearchScreenUiAction.OnQueryChanged -> {
                _uiState.update { it.copy(query = action.query) }
            }
            is SearchScreenUiAction.OnSearchSubmitted -> executeSearch()
            is SearchScreenUiAction.OnSearchResultSelected -> {
                _uiState.update { it.copy(selectedSearchResult = action.result) }
            }
        }
    }

    private fun executeSearch() {

        val currentQuery = _uiState.value.query
        if (currentQuery.isBlank()) return

        searchJob?.cancel()

        searchJob = searchUseCase.execute(currentQuery)
            .onEach { status ->
                _uiState.update { it.copy(searchOperation = status) }
            }
            .launchIn(viewModelScope)
    }
}

@Stable
data class SearchScreenUiState(
    val query: String = "",
    val searchOperation: TaskProgress<List<SearchResult>> = TaskProgress.Idle,
    val selectedSearchResult: SearchResult? = null,
)

sealed interface SearchScreenUiAction {
    data class OnQueryChanged(val query: String): SearchScreenUiAction
    data object OnSearchSubmitted: SearchScreenUiAction
    data class OnSearchResultSelected(val result: SearchResult?): SearchScreenUiAction
}

@Composable
fun searchViewModel(): SearchScreenViewModel {
    val container = LocalDIContainer.current
    return viewModel(
        factory = viewModelFactory {
            initializer {
                SearchScreenViewModel(
                    container.createSearchUseCase()
                )
            }
        }
    )
}