package the.autarch.newsgrid.search.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import the.autarch.newsgrid.search.api.FeedSearchDevApi

class SearchStore(private val searchApi: FeedSearchDevApi) {

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    suspend fun search(query: String) {
        _loading.value = true
        try {
            _searchResults.value = searchApi.search(query)
        } catch (t: Throwable) {
            // TODO: log
        } finally {
            _loading.value = false
        }
    }
}