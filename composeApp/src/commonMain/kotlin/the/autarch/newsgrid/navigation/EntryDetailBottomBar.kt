package the.autarch.newsgrid.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kotlinx.coroutines.launch
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.ic_bookmark
import newsgrid.composeapp.generated.resources.ic_bookmark_border
import newsgrid.composeapp.generated.resources.ic_copy
import newsgrid.composeapp.generated.resources.ic_share
import newsgrid.composeapp.generated.resources.ic_three_dots
import org.jetbrains.compose.resources.painterResource
import the.autarch.newsgrid.LocalSnackbarHostState
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.share.rememberShareManager

@Composable
fun EntryDetailBottomBar(entryId: String, isBookmarked: Boolean) {

    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val snackbar = LocalSnackbarHostState.current
    val store = LocalChannelStore.current
    val uriHandler = LocalUriHandler.current
    val shareManager = rememberShareManager()

    var menuShowing by remember { mutableStateOf(false) }

    BottomAppBar(
        actions = {

            Spacer(Modifier.Companion.weight(1f))

            IconButton({ scope.launch {
                clipboard.setText(AnnotatedString(sanitizeLink(entryId)))
                snackbar.showSnackbar("Link copied to clipboard")
            }}) {
                Icon(
                    painterResource(Res.drawable.ic_copy),
                    contentDescription = "Copy to clipboard"
                )
            }

            IconButton({ scope.launch {
                shareManager.shareText(sanitizeLink(entryId))
            }}) {
                Icon(
                    painterResource(Res.drawable.ic_share),
                    contentDescription = "Share"
                )
            }

            IconButton({ scope.launch {
                if (isBookmarked) {
                    store.removeBookmark(entryId)
                } else {
                    store.saveBookmark(entryId)
                }
            }}) {
                val iconRes = if (isBookmarked) Res.drawable.ic_bookmark else Res.drawable.ic_bookmark_border
                Icon(
                    painterResource(iconRes),
                    contentDescription = "Bookmark"
                )
            }

            Box {

                IconButton({ menuShowing = !menuShowing }) {
                    Icon(
                        painterResource(Res.drawable.ic_three_dots),
                        contentDescription = "More"
                    )
                }

                DropdownMenu(
                    expanded = menuShowing,
                    onDismissRequest = { menuShowing = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Open in browser") },
                        onClick = {
                            menuShowing = false
                            uriHandler.openUri(entryId)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Search on Archive.today") },
                        onClick = {
                            menuShowing = false
                            uriHandler.openUri(encodeArchiveLink(entryId))
                        }
                    )
                }
            }
        },
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

private fun sanitizeLink(link: String): String {
    val url = Url(link)
    return URLBuilder(
        protocol = url.protocol,
        host = url.host,
        port = url.port,
        user = url.user,
        password = url.password,
        pathSegments = url.pathSegments
    ).build().toString()
}

private fun encodeArchiveLink(link: String): String {
    val urlString = sanitizeLink(link)
    return URLBuilder(
        protocol = URLProtocol.Companion.HTTPS,
        host = "archive.is",
        pathSegments = listOf("latest", urlString)
    ).build().toString()
}