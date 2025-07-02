package the.autarch.newsgrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

@Composable
actual fun rememberDataStore(): DataStore<Preferences> = remember { createDataStore() }

fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        File(System.getProperty("java.io.tmpdir"), dataStoreFileName).absolutePath
    }
)