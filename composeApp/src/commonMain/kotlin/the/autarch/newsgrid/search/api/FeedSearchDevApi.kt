package the.autarch.newsgrid.search.api

import androidx.compose.runtime.staticCompositionLocalOf
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import the.autarch.newsgrid.search.data.SearchResult
import the.autarch.newsgrid.search.data.previewData

interface FeedSearchDevApi {

    @GET("search")
    suspend fun search(@Query("url") urlQuery: String): List<SearchResult>
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
    val ktorfit = Ktorfit.Builder()
        .httpClient(ktorClient)
        .baseUrl("https://feedsearch.dev/api/v1/")
        .build()

    return ktorfit.createFeedSearchDevApi()
}

val LocalSearchApi = staticCompositionLocalOf<FeedSearchDevApi> {
    error("No CompositionLocal LocalSearchApi")
}