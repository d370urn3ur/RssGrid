package the.autarch.newsgrid.entry.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import the.autarch.newsgrid.LocalNavHostController
import the.autarch.newsgrid.bookmark.presentation.BookmarkIcon
import the.autarch.newsgrid.entry.data.EntryEntity
import the.autarch.newsgrid.navigation.Route

@Composable
fun EntryItem(item: EntryEntity, channelTitle: String, resolvedBgColor: Color, isBookmarked: Boolean) {

    val navController = LocalNavHostController.current
    val itemSize = 150.dp.let {
        Modifier.size(width = it, height = it)
    }

    Card(
        modifier = Modifier.clickable {
            navController.navigate(Route.EntryDetails(item.link, channelTitle))
        },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
    ) {
        Box(modifier = itemSize.background(resolvedBgColor), contentAlignment = Alignment.BottomStart) {

            item.imageUrl?.let { imageUrl ->
                CoilImage(
                    imageModel = { imageUrl },
                    modifier = itemSize,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    item.title,
                    overflow = TextOverflow.Ellipsis, maxLines = 3, minLines = 3,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                )
            }

            if (isBookmarked) {
                BookmarkIcon(
                    Modifier.padding(4.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
@Preview
fun EntryItemPreview() {
    LazyColumn {
//        items(EntryEntity.previewData()) {
//            EntryItem(it, "Ars Technica - All Content")
//        }
    }
}