package the.autarch.android.newsgrid

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "NewsGrid",
    ) {
        App()
    }
}