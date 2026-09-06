package the.autarch.newsgrid

import android.content.ClipData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ClipEntry

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
actual fun String.toClipEntry(): ClipEntry =
    ClipEntry(ClipData.newPlainText("data", this))