package the.autarch.android.newsgrid.navigation

import kotlinx.serialization.Serializable
import the.autarch.android.newsgrid.entry.data.Entry
import the.autarch.android.newsgrid.entry.data.fromParcelized

class Routes {

    @Serializable
    data object AppContainer

    @Serializable
    data object Search

    @Serializable
    data class EntryDetails(val entryJson: String, val channelTitle: String) {
        val entry: Entry
            get() = Entry.fromParcelized(entryJson)
    }
}