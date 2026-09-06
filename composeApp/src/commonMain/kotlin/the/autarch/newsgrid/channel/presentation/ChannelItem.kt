package the.autarch.newsgrid.channel.presentation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import io.ktor.http.Url
import kotlinx.coroutines.launch
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.ic_check_circle
import org.jetbrains.compose.resources.painterResource
import the.autarch.newsgrid.channel.data.ChannelAndAllEntries
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.entry.presentation.EntryItem
import the.autarch.newsgrid.navigation.Route

@Composable
fun ChannelItem(
    feed: ChannelAndAllEntries,
    selectedChannels: List<ChannelEntity>,
    onChannelSelected: (ChannelEntity) -> Unit,
    onNavigateToRoute: (Route) -> Unit
) {

    val channel = feed.channel
    val entries = feed.entries.sortedByDescending { it.timestamp }
    val channelTitle = channel?.title ?: "Unknown channel"
    val bookmarks by LocalChannelStore.current.bookmarks.collectAsStateWithLifecycle(emptyList())
    val bookmarkIds = bookmarks.map { it.link }
    val entriesListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val networkLoader = rememberNetworkLoader()
    val colorHint = rememberDominantColorState(loader = networkLoader)
    LaunchedEffect(channel?.imageUrl) {
        channel?.imageUrl?.let {
            colorHint.updateFrom(Url(it))
        }
    }

    Column {

        Row(verticalAlignment = Alignment.CenterVertically) {
            ChannelItemIcon(channel, selectedChannels.contains(channel)) {
                channel?.let { onChannelSelected(it) }
            }
            Text(
                channelTitle,
                modifier = Modifier
                    .clickable { scope.launch {
                        entriesListState.animateScrollToItem(0)
                    } }
                    .basicMarquee(),
                overflow = TextOverflow.Visible,
                maxLines = 1
            )
        }
        LazyRow(
            state = entriesListState,
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(entries) { entry ->
                EntryItem(
                    entry,
                    channelTitle,
                    colorHint.color,
                    bookmarkIds.contains(entry.link),
                    onNavigateToRoute
                )
            }
        }
    }
}

@Composable
fun ChannelItemIcon(channel: ChannelEntity?, isSelected: Boolean, onSelected: () -> Unit) {

    val animSpec: AnimationSpec<Float> = tween(300)
    val rotateIconFrontY by animateFloatAsState(
        targetValue = if (isSelected) 180f else 0f,
        animationSpec = animSpec
    )
    val rotateIconBackY by animateFloatAsState(
        targetValue = if (isSelected) 0f else 180f,
        animationSpec = animSpec
    )
    val crossFadeFront by animateFloatAsState(
        targetValue = if (isSelected) 0f else 1f,
        animationSpec = animSpec
    )
    val crossFadeBack by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = animSpec
    )

    Box(
        modifier = Modifier.clickable { onSelected() },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painterResource(Res.drawable.ic_check_circle),
            contentDescription = null,
            modifier = Modifier
                .alpha(crossFadeBack)
                .graphicsLayer {
                    rotationY = rotateIconBackY
                    cameraDistance = 10f
                }
                .padding(16.dp).height(32.dp)
        )

        CoilImage(
            imageModel = { channel?.imageUrl },
            modifier = Modifier
                .alpha(crossFadeFront)
                .graphicsLayer {
                    rotationY = rotateIconFrontY
                    cameraDistance = 10f
                }
                .padding(16.dp).height(32.dp),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        )
    }
}