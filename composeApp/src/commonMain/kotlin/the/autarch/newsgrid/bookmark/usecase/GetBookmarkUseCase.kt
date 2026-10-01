package the.autarch.newsgrid.bookmark.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkEntity
import the.autarch.newsgrid.bookmark.data.BookmarkRepository

class GetBookmarkUseCase(private val repo: BookmarkRepository) {

    fun execute(bookmarkId: String): Flow<TaskProgress<BookmarkEntity>> = flow {
        val result = repo.getBookmark(bookmarkId)
        if (result != null) {
            emit(TaskProgress.Success(result))
        } else {
            emit(TaskProgress.Failure(RuntimeException("No bookmark found with id: $bookmarkId")))
        }
    }
}