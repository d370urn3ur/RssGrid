package the.autarch.android.newsgrid.share

import androidx.compose.runtime.Composable

expect class ShareManager {
    fun shareText(text: String)
}

@Composable
expect fun rememberShareManager(): ShareManager