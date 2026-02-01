package the.autarch.newsgrid

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import okio.Path.Companion.toPath

internal const val dataStoreFileName = "news_grid.preferences_pb"
internal val LAST_UPDATE = longPreferencesKey("last_update")

fun getPreferencesDataStore(path: String) = PreferenceDataStoreFactory.createWithPath {
    path.toPath()
}

expect fun createDataStore(): DataStore<Preferences>