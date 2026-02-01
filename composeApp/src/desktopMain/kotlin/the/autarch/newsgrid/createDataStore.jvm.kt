package the.autarch.newsgrid

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun getPreferencesDataStorePath(): String {
    return File(System.getProperty("java.io.tmpdir"), dataStoreFileName).absolutePath
}

actual fun createDataStore(): DataStore<Preferences> {
    val path = getPreferencesDataStorePath()
    return getPreferencesDataStore(path)
}