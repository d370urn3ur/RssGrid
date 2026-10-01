package the.autarch.newsgrid.bookmark.data

import the.autarch.newsgrid.AppDatabase

class BookmarkRepository(private val appDatabase: AppDatabase) {

    val bookmarks = appDatabase.getBookmarkDao().getAllAsFlow()

    suspend fun getBookmark(bookmarkId: String): BookmarkEntity? =
        appDatabase.getBookmarkDao().bookmarkForId(bookmarkId)

    suspend fun saveBookmark(entryId: String) {
        val entry = appDatabase.getEntryDao().entryForId(entryId)
        entry?.let {
            appDatabase.getChannelDao().channelForId(entry.channelId)?.let { channel ->
                val bookmark = BookmarkEntity(
                    link = entry.link,
                    title = entry.title,
                    description = entry.description,
                    content = entry.content,
                    pubDate = entry.pubDate,
                    timestamp = entry.timestamp,
                    author = entry.author,
                    imageUrl = entry.imageUrl,
                    source = entry.source,
                    channelName = channel.title,
                    channelImageUrl = channel.imageUrl
                )
                appDatabase.getBookmarkDao().insert(bookmark)
            }
        }
    }

    suspend fun removeBookmark(entryId: String) {
        appDatabase.getBookmarkDao().bookmarkForId(entryId)?.let {
            appDatabase.getBookmarkDao().delete(it)
        }
    }
}