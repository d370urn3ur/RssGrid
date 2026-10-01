package the.autarch.newsgrid.search.data

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import the.autarch.newsgrid.search.api.FeedSearchDevApi
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.search.usecase.SearchUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SearchUseCaseTest {

    @Test
    fun `execute emits Loading followed by Success`() = runTest {
        // Given
        val expectedResults = listOf(
            SearchResult(title = "Ars Technica", url = "https://arstechnica")
        )
        val fakeApi = FakeFeedSearchDevApi(resultsToReturn = expectedResults)
        val useCase = SearchUseCase(fakeApi)

        // When
        val emissions = useCase.execute("arstechnica.com").toList()

        // Then
        assertEquals(2, emissions.size)
        assertEquals(TaskProgress.Running(), emissions[0])
        assertEquals(TaskProgress.Success(expectedResults), emissions[1])
    }

    @Test
    fun `execute emits Running followed by Failure when API fails`() = runTest {
        // Given
        val fakeApi = FakeFeedSearchDevApi(shouldThrowError = true)
        val useCase = SearchUseCase(fakeApi)

        // When
        val emissions = useCase.execute("error.com").toList()

        // Then
        assertEquals(2, emissions.size)
        assertEquals(TaskProgress.Running(), emissions[0])
        assertIs<TaskProgress.Failure>(emissions[1])
    }

}

class FakeFeedSearchDevApi(
    var resultsToReturn: List<SearchResult> = emptyList(),
    var shouldThrowError: Boolean = false
): FeedSearchDevApi {

    override suspend fun search(urlQuery: String): List<SearchResult> {
        if (shouldThrowError) {
            throw Exception("Network Error")
        }
        return resultsToReturn
    }
}