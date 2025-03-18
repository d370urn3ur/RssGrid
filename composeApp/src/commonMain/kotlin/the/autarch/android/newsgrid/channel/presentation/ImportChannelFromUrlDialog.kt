package the.autarch.android.newsgrid.channel.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import the.autarch.android.newsgrid.LocalSnackbarHostState
import the.autarch.android.newsgrid.channel.data.LocalChannelStore

@Composable
fun ImportChannelFromUrlDialog(scope: CoroutineScope, onDismiss: () -> Unit) {

    val (importUrl, setImportUrl) = remember { mutableStateOf("") }
    var errorText: String? by remember { mutableStateOf(null) }

    val channelStore = LocalChannelStore.current
    val snackbarHostState = LocalSnackbarHostState.current

    AlertDialog(
//            icon = {
//                Icon(icon, contentDescription = "Example Icon")
//            },
        title = {
            Text(text = "Import channel from URL")
        },
        text = {
            TextField(
                importUrl,
                setImportUrl,
                supportingText = {
                    errorText?.let { Text(it) }
                },
                isError = errorText != null
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton({
                scope.launch {
                    try {
                        channelStore.addChannel(importUrl)
                        onDismiss()
                        snackbarHostState.showSnackbar("Added channel: $importUrl")
                    } catch (t: Throwable) {
                        errorText = t.message
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