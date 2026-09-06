package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import com.skydoves.landscapist.coil3.CoilImage
import io.ktor.http.Url
import kotlinx.coroutines.launch
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.bookmark.data.BookmarkSummary
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.navigation.Route

@Composable
fun BookmarkItem(bookmark: BookmarkSummary, modifier: Modifier = Modifier) {

    val store = LocalChannelStore.current
    val scope = rememberCoroutineScope()

    val networkLoader = rememberNetworkLoader()
    val colorHint = rememberDominantColorState(loader = networkLoader)
    LaunchedEffect(bookmark.channelImageUrl) {
        bookmark.channelImageUrl?.let {
            colorHint.updateFrom(Url(it))
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Card(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        ) {
            Box(
                modifier = Modifier.background(colorHint.color),
                contentAlignment = Alignment.BottomEnd
            ) {

                CoilImage(
                    imageModel = { bookmark.imageUrl },
                    Modifier.fillMaxWidth()
                        .aspectRatio(16f / 9f),
                )

                TextButton({
                    scope.launch {
                        store.removeBookmark(bookmark.link)
                    }
                }) {
                    BookmarkIcon(Modifier.padding(4.dp))
                }
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

        bookmark.pubDate?.let {
            Text(it, style = MaterialTheme.typography.bodySmall)
        }
    }
}