package the.autarch.newsgrid.entry.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import kotlinx.coroutines.launch
import the.autarch.newsgrid.jostRegularItalic
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.entry.data.Entry
import the.autarch.newsgrid.entry.data.EntryEntity

@Composable
fun EntryDetailsScreen(entryId: String) {

    val scope = rememberCoroutineScope()
    val store = LocalChannelStore.current
    var entry by remember { mutableStateOf<EntryEntity?>(null) }

    LaunchedEffect(entryId) {
        scope.launch {
            entry = store.getEntry(entryId)
        }
    }

    entry?.let { entry ->
        EntryDetailsScreenContent(entry)
    }
}

@Composable
fun EntryDetailsScreenContent(entry: Entry) {

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            entry.title,
            style = MaterialTheme.typography.titleLarge
        )

        if (entry.author != null || entry.pubDate != null) {

            Column {

                entry.author?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                entry.pubDate?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        entry.description?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodyLarge.copy(fontFamily = jostRegularItalic())
            )
        }

        entry.content?.let {
            Text(
                remember { htmlToAnnotatedString(it) },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        val uriHandler = LocalUriHandler.current
        Button(
            { uriHandler.openUri(entry.link) },
            Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Open in browser")
        }
    }
}