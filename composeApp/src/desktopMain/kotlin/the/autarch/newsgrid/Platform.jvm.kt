package the.autarch.newsgrid

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import java.awt.datatransfer.StringSelection

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

@OptIn(ExperimentalComposeUiApi::class)
actual fun String.toClipEntry(): ClipEntry =
    ClipEntry(StringSelection(this))