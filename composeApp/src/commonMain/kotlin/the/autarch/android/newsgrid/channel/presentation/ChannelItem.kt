package the.autarch.android.newsgrid.channel.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import the.autarch.android.newsgrid.channel.data.ChannelAndAllEntries
import the.autarch.android.newsgrid.channel.data.LocalChannelStore
import the.autarch.android.newsgrid.entry.presentation.EntryItem

@Composable
fun ChannelItem(feed: ChannelAndAllEntries) {

    val channel = feed.channel
    val channelTitle = channel?.title ?: "Unknown channel"
    val bookmarks by LocalChannelStore.current.bookmarks.collectAsStateWithLifecycle(emptyList())
    val bookmarkIds = bookmarks.map { it.link }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
                imageModel = { channel?.imageUrl },
                modifier = Modifier.padding(16.dp).size(width = 32.dp, height = 32.dp),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            )
            Text(channelTitle)
        }
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(feed.entries) { entry ->
                EntryItem(entry, channelTitle, bookmarkIds.contains(entry.link))
            }
        }
    }
}

@Preview
@Composable
fun ChannelItemPreview() {
//    LazyColumn {
//        items(Channel.previewData()) {
//            ChannelItem(it)
//        }
//    }
}