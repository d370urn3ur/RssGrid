package the.autarch.newsgrid.channel.presentation

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PlatformImeOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import the.autarch.newsgrid.LocalSnackbarHostState
import the.autarch.newsgrid.channel.data.LocalChannelStore

@Composable
fun ImportChannelFromUrlDialog(scope: CoroutineScope, onDismiss: () -> Unit) {

    val channelStore = LocalChannelStore.current
    val snackbarHostState = LocalSnackbarHostState.current

    val (importUrl, setImportUrl) = remember { mutableStateOf("") }
    var errorText: String? by remember { mutableStateOf(null) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                Modifier.focusRequester(focusRequester),
                supportingText = {
                    errorText?.let { Text(it) }
                },
                isError = errorText != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri
                )
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