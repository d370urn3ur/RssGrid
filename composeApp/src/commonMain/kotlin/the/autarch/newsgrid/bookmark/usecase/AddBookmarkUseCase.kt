package the.autarch.newsgrid.bookmark.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkRepository

class AddBookmarkUseCase(
    private val bookmarkRepo: BookmarkRepository
) {

    fun execute(bookmarkId: String): Flow<TaskProgress<Unit>> = flow {
        bookmarkRepo.saveBookmark(bookmarkId)
        emit(TaskProgress.Success(Unit))
    }
}