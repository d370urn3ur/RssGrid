package the.autarch.newsgrid

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import the.autarch.newsgrid.bookmark.data.BookmarkRepository
import the.autarch.newsgrid.bookmark.usecase.AddBookmarkUseCase
import the.autarch.newsgrid.bookmark.usecase.GetBookmarkUseCase
import the.autarch.newsgrid.bookmark.usecase.RemoveBookmarkUseCase
import the.autarch.newsgrid.channel.api.provideRssParser
import the.autarch.newsgrid.channel.data.ChannelRepository
import the.autarch.newsgrid.channel.usecase.AddChannelUseCase
import the.autarch.newsgrid.channel.usecase.DeleteChannelsUseCase
import the.autarch.newsgrid.channel.usecase.MoveChannelUseCase
import the.autarch.newsgrid.channel.usecase.RefreshChannelsUseCase
import the.autarch.newsgrid.entry.usecase.GetEntryUseCase
import the.autarch.newsgrid.search.api.FeedSearchDevApi
import the.autarch.newsgrid.search.api.provideSearchApi
import the.autarch.newsgrid.search.usecase.SearchUseCase

class DIContainer(
    val dataStore: DataStore<Preferences>,
    appDatabase: AppDatabase,
) {

    private val rssParser = provideRssParser()
    private val searchApi: FeedSearchDevApi by lazy { provideSearchApi() }

    val channelRepository = ChannelRepository(appDatabase, rssParser)
    val bookmarkRepo: BookmarkRepository = BookmarkRepository(appDatabase)

    fun createSearchUseCase(): SearchUseCase = SearchUseCase(searchApi)

    fun createAddChannelUseCase(): AddChannelUseCase = AddChannelUseCase(channelRepository)
    fun createDeleteChannelUseCase(): DeleteChannelsUseCase = DeleteChannelsUseCase(channelRepository)
    fun createGetEntryUseCase(): GetEntryUseCase = GetEntryUseCase(channelRepository)
    fun createMoveChannelUseCase(): MoveChannelUseCase = MoveChannelUseCase(channelRepository)
    fun createRefreshChannelsUseCase(): RefreshChannelsUseCase = RefreshChannelsUseCase(
        channelRepository,
        dataStore
    )

    fun createGetBookmarkUseCase(): GetBookmarkUseCase = GetBookmarkUseCase(bookmarkRepo)
    fun createAddBookmarkUseCase(): AddBookmarkUseCase = AddBookmarkUseCase(bookmarkRepo)
    fun createRemoveBookmarkUseCase(): RemoveBookmarkUseCase = RemoveBookmarkUseCase(bookmarkRepo)
}

var LocalDIContainer = staticCompositionLocalOf<DIContainer> {
    error("No LocalDIContainer provided")
}