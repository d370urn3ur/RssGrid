package the.autarch.newsgrid

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

private lateinit var appContext: Context

fun initPreferencesDataStore(context: Context) {
    appContext = context.applicationContext
}

fun getPreferencesDataStorePath(appContext: Context): String =
    appContext.filesDir.resolve(dataStoreFileName).absolutePath

actual fun createDataStore(): DataStore<Preferences> {
    val path = getPreferencesDataStorePath(appContext)
    return getPreferencesDataStore(path)
}