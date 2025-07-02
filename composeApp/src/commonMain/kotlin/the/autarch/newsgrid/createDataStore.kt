package the.autarch.newsgrid

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import okio.Path.Companion.toPath

@Composable
expect fun rememberDataStore(): DataStore<Preferences>

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "news_grid.preferences_pb"
internal val LAST_UPDATE = longPreferencesKey("last_update")