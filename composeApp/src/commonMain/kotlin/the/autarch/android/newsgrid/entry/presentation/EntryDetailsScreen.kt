package the.autarch.android.newsgrid.entry.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import the.autarch.android.newsgrid.entry.data.Entry

@Composable
fun EntryDetailsScreen(entry: Entry) {

    Column {

        Column(
            modifier = Modifier.padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(entry.title)

            entry.author?.let {
                Text(it)
            }

            entry.pubDate?.let {
                Text(it)
            }

            entry.description?.let {
                Text(it)
            }

            entry.content?.let {
                Text(it)
            }

            val uriHandler = LocalUriHandler.current
            ElevatedButton({
                uriHandler.openUri(entry.link)
            }) {
                Text("Open in browser")
            }
        }
    }
}