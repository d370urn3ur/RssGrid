package the.autarch.newsgrid.entry.data

import kotlinx.datetime.Instant
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.FormatStringsInDatetimeFormats

interface Entry {
    val link: String
    val title: String
    val description: String?
    val content: String?
    val pubDate: String?
    val timestamp: Instant?
    val author: String?

    companion object {
        @OptIn(FormatStringsInDatetimeFormats::class)
        val formatters = listOf(
            DateTimeComponents.Formats.RFC_1123
        )
    }
}