package the.autarch.newsgrid.entry.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import io.github.adrcotfas.datetime.names.FormatStyle
import io.github.adrcotfas.datetime.names.format
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.entry.data.Entry
import the.autarch.newsgrid.entry.data.EntryEntity
import kotlin.time.Instant

@Composable
fun EntryDetailsScreen(
    entryId: String,
    viewModel: EntryDetailsViewModel = entryDetailsViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(entryId) {
        viewModel.getEntry(entryId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val status = uiState.getEntryOperation) {
            is TaskProgress.Success -> EntryDetailsScreenContent(status.result)
            is TaskProgress.Failure -> {
                // TODO: show error screen
            }
            else -> {
                // TODO: show circularprogress
            }
        }
    }
}

@Stable
data class EntryDetailsUiState(
    val getEntryOperation: TaskProgress<EntryEntity> = TaskProgress.Idle
)

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

        if (entry.author != null || entry.pubDate != null || entry.timestamp != null) {

            Column {

                entry.author?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                val timestamp = entry.timestamp
                if (timestamp != null) {

                    val localDt = Instant.fromEpochMilliseconds(timestamp)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .format(dateStyle = FormatStyle.SHORT, timeStyle = FormatStyle.SHORT)
                    Text(
                        localDt,
                        style = MaterialTheme.typography.labelMedium
                    )

                } else {

                    entry.pubDate?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        entry.description?.let {
            Text(
                remember { htmlToAnnotatedString(it) },
                style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic)
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

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewEntryDetailsScreen() {
//    EntryDetailsScreenContent(
//        Entry()
//    )
//}