package the.autarch.android.newsgrid.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import the.autarch.android.newsgrid.LocalSnackbarHostState
import the.autarch.android.newsgrid.channel.data.LocalChannelStore
import the.autarch.android.newsgrid.search.data.SearchResult

@Composable
fun SearchResultDetailsDialog(searchResult: SearchResult, scope: CoroutineScope, onDismiss: () -> Unit) {

    AlertDialog(
        title = {
            Text("Do you want to add this channel?")
        },
        text = {
            Column {
                Text(searchResult.title)
                searchResult.description?.let {
                    Text(it)
                }
                Text(searchResult.url)
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {

            val channelStore = LocalChannelStore.current
            val snackbarHostState = LocalSnackbarHostState.current

            TextButton({
                scope.launch {
                    try {
                        channelStore.addChannel(searchResult.url)
                        onDismiss()
                        snackbarHostState.showSnackbar("Added channel: ${searchResult.url}")
                    } catch (t: Throwable) {
                        onDismiss()
                        snackbarHostState.showSnackbar("Error adding channel: ${t.message}")
                    }
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onDismiss) {
                Text("Cancel")
            }
        }
    )
}