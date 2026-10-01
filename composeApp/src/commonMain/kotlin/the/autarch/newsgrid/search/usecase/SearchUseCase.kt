package the.autarch.newsgrid.search.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.logging.AppLog
import the.autarch.newsgrid.search.api.FeedSearchDevApi
import the.autarch.newsgrid.search.data.SearchResult

class SearchUseCase(
    private val searchApi: FeedSearchDevApi
) {

    fun execute(query: String): Flow<TaskProgress<List<SearchResult>>> = flow {

        emit(TaskProgress.Running())

        try {
            val response = searchApi.search(query)
            emit(TaskProgress.Success(response))
        } catch (t: Throwable) {
            AppLog.error(t, { "error searching for: $query" }, AppLog.Category.NETWORK)
            emit(TaskProgress.Failure(t))
        }
    }
}