package the.autarch.android.newsgrid.channel.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import the.autarch.android.newsgrid.entry.presentation.EntryItem
import the.autarch.android.newsgrid.channel.data.Channel
import the.autarch.android.newsgrid.channel.data.previewData

@Composable
fun ChannelItem(channel: Channel) {

    val channelTitle = channel.title ?: "Unknown channel"

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CoilImage(
                imageModel = { channel.imageUrl },
                modifier = Modifier.padding(16.dp).width(24.dp).height(24.dp),
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            )
            Text(channelTitle)
        }
        LazyRow {
            items(channel.items) {
                EntryItem(it, channelTitle)
            }
        }
    }
}

@Preview
@Composable
fun ChannelItemPreview() {
    LazyColumn {
        items(Channel.previewData()) {
            ChannelItem(it)
        }
    }
}