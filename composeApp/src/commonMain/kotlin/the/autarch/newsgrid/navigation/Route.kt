package the.autarch.newsgrid.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object AppContainer: Route

    @Serializable
    data object Search: Route

    @Serializable
    data class EntryDetails(val entryId: String, val channelTitle: String): Route

    @Serializable
    data class BookmarkDetails(val bookmarkId: String, val channelTitle: String): Route
}