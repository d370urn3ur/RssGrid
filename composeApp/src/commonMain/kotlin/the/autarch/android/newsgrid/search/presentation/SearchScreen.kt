package the.autarch.android.newsgrid.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import the.autarch.android.newsgrid.search.api.LocalSearchApi
import the.autarch.android.newsgrid.search.data.SearchResult
import the.autarch.android.newsgrid.search.data.SearchStore

@Composable
fun SearchScreen() {

    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val api = LocalSearchApi.current
    val searchStore = remember { SearchStore(api) }
    val searchResults by searchStore.searchResults.collectAsStateWithLifecycle()
    val loading by searchStore.loading.collectAsStateWithLifecycle()
    var showSearchResultDetailsDialog by remember { mutableStateOf<SearchResult?>(null) }

    Column(
        modifier = Modifier.padding(16.dp),
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
                value = searchText,
                onValueChange = setSearchText,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        scope.launch {
                            searchStore.search(searchText)
                        }
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

        if (loading) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) {
                    SearchResultItem(it) {
                        showSearchResultDetailsDialog = it
                    }
                }
            }
        }

        showSearchResultDetailsDialog?.let {
            SearchResultDetailsDialog(it, scope) {
                showSearchResultDetailsDialog = null
            }
        }
    }
}