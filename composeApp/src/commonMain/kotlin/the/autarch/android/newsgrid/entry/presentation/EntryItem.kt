package the.autarch.android.newsgrid.entry.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import the.autarch.android.newsgrid.LocalNavHostController
import the.autarch.android.newsgrid.entry.data.Entry
import the.autarch.android.newsgrid.entry.data.parcelize
import the.autarch.android.newsgrid.entry.data.previewData
import the.autarch.android.newsgrid.navigation.Routes

@Composable
fun EntryItem(item: Entry, channelTitle: String) {

    val navController = LocalNavHostController.current

    Card(
        modifier = Modifier.clickable {
            navController.navigate(Routes.EntryDetails(item.parcelize(), channelTitle))
        },
        shape = RoundedCornerShape(percent = 50),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
    ) {
        Box(modifier = Modifier.width(150.dp)) {
            Text(item.title, maxLines = 3)
        }
    }
}

@Composable
@Preview
fun EntryItemPreview() {
    LazyColumn {
        items(Entry.previewData()) {
            EntryItem(it, "Ars Technica - All Content")
        }
    }
}