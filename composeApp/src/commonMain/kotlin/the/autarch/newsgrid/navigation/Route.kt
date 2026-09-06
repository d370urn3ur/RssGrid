package the.autarch.newsgrid.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object Main: Route

    @Serializable
    data object Search: Route

    @Serializable
    data class EntryDetails(val entryId: String, val channelTitle: String): Route

    @Serializable
    data class BookmarkDetails(val bookmarkId: String, val channelTitle: String): Route
}

@OptIn(ExperimentalSerializationApi::class)
val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Route>()
        }
    }
}