package the.autarch.newsgrid.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import the.autarch.newsgrid.search.data.SearchResult

@Composable
fun SearchResultDetailsDialog(searchResult: SearchResult, onAddChannel: () -> Unit, onDismiss: () -> Unit) {

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
            TextButton(onAddChannel) {
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