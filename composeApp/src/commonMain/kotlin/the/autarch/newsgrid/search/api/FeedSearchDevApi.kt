package the.autarch.newsgrid.search.api

import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.appendEncodedPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import the.autarch.newsgrid.search.data.SearchResult
import the.autarch.newsgrid.search.data.previewData

interface FeedSearchDevApi {
    suspend fun search(urlQuery: String): List<SearchResult>
}

data class FeedSearchDevApiImpl(private val client: HttpClient): FeedSearchDevApi {

    override suspend fun search(urlQuery: String): List<SearchResult> {
        return client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "feedsearch.dev"
                appendEncodedPathSegments("api", "v1", "search")
                parameters.append("url", urlQuery)
            }
        }.body()
    }
}

class PreviewFeedSearchDevApi: FeedSearchDevApi {
    override suspend fun search(urlQuery: String): List<SearchResult> =
        SearchResult.previewData()
}

fun provideSearchApi(): FeedSearchDevApi {
    val ktorClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true })
        }
    }
    return FeedSearchDevApiImpl(ktorClient)
}

val LocalSearchApi = staticCompositionLocalOf<FeedSearchDevApi> {
    error("No CompositionLocal LocalSearchApi")
}