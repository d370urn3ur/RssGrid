package the.autarch.newsgrid.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import the.autarch.newsgrid.AppUiAction
import the.autarch.newsgrid.LocalSnackbarHostState
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.search.data.SearchResult

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel = searchViewModel(),
    addChannelOperation: TaskProgress<String>,
    onAddChannel: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(addChannelOperation) {
        when (addChannelOperation) {
            is TaskProgress.Success -> {
                viewModel.onAction(SearchScreenUiAction.OnSearchResultSelected(null))
            }
            is TaskProgress.Failure -> {
                viewModel.onAction(SearchScreenUiAction.OnSearchResultSelected(null))
            }
            else -> {}
        }
    }

    SearchScreenContent(uiState, {
        viewModel.onAction(it)
    }, onAddChannel)
}

@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    onAction: (SearchScreenUiAction) -> Unit,
    onAddChannel: (String) -> Unit
) {

    val searchOp = uiState.searchOperation

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val uriHandler = LocalUriHandler.current

        Text(
            "Search powered by Feedsearch",
            modifier = Modifier.clickable {
                uriHandler.openUri("https://feedsearch.dev")
            }
        )

        ElevatedCard(
            shape = RoundedCornerShape(percent = 50),
            colors = CardColors(
                containerColor = Color(0xffefefef),
                contentColor = Color.Blue,
                disabledContainerColor = Color.Green,
                disabledContentColor = Color.Cyan
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
        ) {
            TextField(
                value = uiState.query,
                onValueChange = { onAction(SearchScreenUiAction.OnQueryChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onAction(SearchScreenUiAction.OnSearchSubmitted)
                    }
                ),
                singleLine = true,
                placeholder = { Text("Site or Feed URL") },
                shape = RoundedCornerShape(percent = 50),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )
        }

        when (searchOp) {

            is TaskProgress.Running -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is TaskProgress.Success -> {
                if (searchOp.result.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchOp.result) {
                            SearchResultItem(it) {
                                onAction(SearchScreenUiAction.OnSearchResultSelected(it))
                            }
                        }
                    }
                }
            }

            else -> Text(
                "Search by domain. \n Ex: arstechnica.com, slashdot.org, cnn.com",
                textAlign = TextAlign.Center
            )
        }

        uiState.selectedSearchResult?.let {
            SearchResultDetailsDialog(
                searchResult = it,
                onAddChannel = { onAddChannel(it.url) },
                onDismiss = {
                    onAction(SearchScreenUiAction.OnSearchResultSelected(null))
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview_Idle() {
    SearchScreenContent(
        uiState = SearchScreenUiState(query = ""),
        onAction = {},
        onAddChannel = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview_Loading() {
    SearchScreenContent(
        uiState = SearchScreenUiState(
            query = "arstechnica.com",
            searchOperation = TaskProgress.Running()
        ),
        onAction = {},
        onAddChannel = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview_SuccessWithResults() {
    SearchScreenContent(
        uiState = SearchScreenUiState(
            query = "arstechnica.com",
            searchOperation = TaskProgress.Success(
                listOf(
                    SearchResult(
                        title = "Ars Technica",
                        url = "https://feeds.arstechnica.com/arstechnica/index",
                        description = "Dev, Tech, Science News"
                    )
                )
            )
        ),
        onAction = {},
        onAddChannel = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview_DialogShowing() {
    val sampleResult = SearchResult(
        title = "Ars Technica Main Feed",
        url = "https://feeds.arstechnica.com/arstechnica/index",
        description = "All news & features"
    )

    SearchScreenContent(
        uiState = SearchScreenUiState(
            query = "arstechnica.com",
            searchOperation = TaskProgress.Success(listOf(sampleResult)),
            selectedSearchResult = sampleResult
        ),
        onAction = {},
        onAddChannel = {}
    )
}