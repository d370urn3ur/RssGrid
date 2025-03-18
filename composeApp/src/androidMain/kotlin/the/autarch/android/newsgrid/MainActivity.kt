package the.autarch.android.newsgrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import the.autarch.android.newsgrid.search.api.LocalSearchApi
import the.autarch.android.newsgrid.search.api.PreviewFeedSearchDevApi
import the.autarch.android.newsgrid.search.presentation.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

@Preview
@Composable
fun SearchScreenPreview() {
    CompositionLocalProvider(
        LocalSearchApi provides PreviewFeedSearchDevApi(),
    ) {
        MaterialTheme {
            SearchScreen()
        }
    }
}