package the.autarch.android.newsgrid.bookmark.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.coroutines.launch
import the.autarch.android.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.android.newsgrid.channel.data.LocalChannelStore

@Composable
fun BookmarkItem(bookmark: BookmarkEntity, modifier: Modifier = Modifier) {

    val store = LocalChannelStore.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box(contentAlignment = Alignment.BottomEnd) {

            CoilImage(
                imageModel = { bookmark.imageUrl },
                Modifier.clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .aspectRatio(16f/9f),
            )

            TextButton({ scope.launch {
                store.removeBookmark(bookmark.link)
            }}) {
                BookmarkIcon(Modifier.padding(4.dp))
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            bookmark.channelImageUrl?.let { channelImage ->
                CoilImage(
                    imageModel = { channelImage },
                    Modifier.clip(CircleShape)
                        .size(24.dp),
                )
            }

            Text(bookmark.channelName, style = MaterialTheme.typography.bodySmall)
        }

        Text(bookmark.title, style = MaterialTheme.typography.titleMedium)

        bookmark.published?.let {
            Text(it, style = MaterialTheme.typography.bodySmall)
        }
    }
}