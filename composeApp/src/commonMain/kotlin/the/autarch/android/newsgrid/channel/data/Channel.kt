package the.autarch.android.newsgrid.channel.data

import the.autarch.android.newsgrid.entry.data.Entry

data class Channel(
    val title: String?,
    val link: String?,   // unique
    val description: String?,
    val imageUrl: String?, // RssImage.url?
    val items: List<Entry>,
) {
    companion object
}