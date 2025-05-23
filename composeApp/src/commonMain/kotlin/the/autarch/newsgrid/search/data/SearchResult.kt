package the.autarch.newsgrid.search.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val title: String,
    val url: String,
    val description: String? = null,
    val favicon: String? = null,
    @SerialName("site_name") val siteName: String? = null,  // site_name
    @SerialName("item_count") val itemCount: Int = 0  // item_count
//    @SerialName("last_updated") val lastUpdated: String? //  "2024-07-01T13:21:36+00:00",
)

fun SearchResult.Companion.previewData(): List<SearchResult> =
    listOf(
        SearchResult(title = "SearchResult1", url = "https://arstechnica.com"),
        SearchResult(title = "SearchResult2", url = "https://arstechnica.com"),
        SearchResult(title = "SearchResult3", url = "https://arstechnica.com"),
    )