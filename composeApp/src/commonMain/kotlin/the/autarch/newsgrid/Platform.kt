package the.autarch.newsgrid

import androidx.compose.ui.platform.ClipEntry

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun String.toClipEntry(): ClipEntry