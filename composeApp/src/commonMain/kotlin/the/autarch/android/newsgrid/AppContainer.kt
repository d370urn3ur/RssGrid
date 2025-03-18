package the.autarch.android.newsgrid

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import the.autarch.android.newsgrid.bookmark.BookmarksScreen
import the.autarch.android.newsgrid.channel.presentation.ChannelsScreen
import the.autarch.android.newsgrid.navigation.TabIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContainer() {

    var tabIndex by remember { mutableStateOf(TabIndex.CHANNELS) }

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
            TabIndex.CHANNELS -> ChannelsScreen()
            TabIndex.BOOKMARKS -> BookmarksScreen()
        }
    }
}