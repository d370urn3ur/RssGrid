package the.autarch.newsgrid.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

actual class ShareManager {
    actual fun shareText(text: String) {
        // Not supported
    }
}

@Composable
actual fun rememberShareManager(): ShareManager = remember { ShareManager() }