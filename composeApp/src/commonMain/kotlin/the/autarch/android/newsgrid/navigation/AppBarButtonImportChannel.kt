package the.autarch.android.newsgrid.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.vertical_align_bottom
import org.jetbrains.compose.resources.painterResource
import the.autarch.android.newsgrid.channel.presentation.ImportChannelFromUrlDialog

@Composable
fun AppBarButtonImportChannel() {

    var showImportDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    IconButton({ showImportDialog = true }) {
        Icon(
            painter = painterResource(Res.drawable.vertical_align_bottom),
            contentDescription = "Import from URL"
        )
    }

    if (showImportDialog) {
        ImportChannelFromUrlDialog(scope) {
            showImportDialog = false
        }
    }
}