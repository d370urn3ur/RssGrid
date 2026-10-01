package the.autarch.newsgrid.bookmark.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkRepository

class RemoveBookmarkUseCase(
    private val bookmarkRepo: BookmarkRepository
) {

    fun execute(bookmarkId: String): Flow<TaskProgress<Unit>> = flow {
        bookmarkRepo.removeBookmark(bookmarkId)
        emit(TaskProgress.Success(Unit))
    }
}