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
import the.autarch.newsgrid.base.TaskProgress

@Composable
fun ImportChannelFromUrlDialog(
    addChannelOperation: TaskProgress<String>,
    onAddChannel: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val (importUrl, setImportUrl) = remember { mutableStateOf("") }
    var errorText: String? by remember { mutableStateOf(null) }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(addChannelOperation) {
        when (addChannelOperation) {
            is TaskProgress.Success -> {
                onDismiss()
            }
            is TaskProgress.Failure -> {
                errorText = addChannelOperation.error.message
            }
            else -> {}
        }
    }

    AlertDialog(
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
                onAddChannel(importUrl)
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