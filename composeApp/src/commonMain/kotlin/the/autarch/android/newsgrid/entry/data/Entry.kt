package the.autarch.android.newsgrid.entry.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Entry(
    val title: String,
    val author: String?,
    val link: String,   // unique
    val pubDate: String?,
    val description: String?,
    val content: String?,
    val image: String?,
//    val audio: String?,
//    val video: String?,
//    val sourceName: String?,
//    val sourceUrl: String?,
//    val categories: List<String>,
//    val rawEnclosure: RawEnclosure,
) {
    companion object
}

fun Entry.parcelize(): String = Json.encodeToString(this)
fun Entry.Companion.fromParcelized(parcel: String): Entry = Json.decodeFromString(parcel)