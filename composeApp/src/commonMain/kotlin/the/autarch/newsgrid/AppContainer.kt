package the.autarch.newsgrid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import io.ktor.http.Url
import the.autarch.newsgrid.bookmark.presentation.BookmarkItem
import the.autarch.newsgrid.bookmark.presentation.BookmarksScreen
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.channel.presentation.ChannelItem
import the.autarch.newsgrid.channel.presentation.ChannelsScreen
import the.autarch.newsgrid.navigation.Route
import the.autarch.newsgrid.navigation.TabIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContainer(selectedChannels: List<ChannelEntity>, onChannelSelected: (ChannelEntity) -> Unit) {

    var tabIndex by remember { mutableStateOf(TabIndex.CHANNELS) }

    val store = LocalChannelStore.current
    val channels by LocalChannelStore.current.channels.collectAsStateWithLifecycle(emptyList())
    val bookmarks by LocalChannelStore.current.bookmarks.collectAsStateWithLifecycle(emptyList())
    val navController = LocalNavHostController.current

    LaunchedEffect(Unit) {
        store.refreshChannels()
    }

    Column {
        PrimaryTabRow(selectedTabIndex = tabIndex.idxVal) {
            TabIndex.entries.forEach {
                Tab(
                    selected = tabIndex == it,
                    onClick = { tabIndex = it },
                    text = { Text(text = it.title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
        when (tabIndex) {
            TabIndex.CHANNELS -> ChannelsScreen(
                channels,
                onNavigateToRoute = { navController.navigate(it) }
            ) { channel ->
                ChannelItem(channel, selectedChannels, onChannelSelected)
            }
            TabIndex.BOOKMARKS -> BookmarksScreen(bookmarks) { bookmark ->
                BookmarkItem(
                    bookmark,
                    Modifier.animateItem()
                        .clickable {
                            navController.navigate(Route.BookmarkDetails(bookmark.link, bookmark.channelName))
                        }
                )
            }
        }
    }
}