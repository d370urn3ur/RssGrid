package the.autarch.android.newsgrid

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import the.autarch.android.newsgrid.channel.api.provideRssParser
import the.autarch.android.newsgrid.channel.data.ChannelStore
import the.autarch.android.newsgrid.channel.data.LocalChannelStore
import the.autarch.android.newsgrid.navigation.AppBar
import the.autarch.android.newsgrid.navigation.AppBottomBar
import the.autarch.android.newsgrid.navigation.AppRouter
import the.autarch.android.newsgrid.search.api.LocalSearchApi
import the.autarch.android.newsgrid.search.api.provideSearchApi

@Composable
@Preview
fun App() {

//    var channels: List<Channel> by remember { mutableStateOf(emptyList()) }
//
//    if (LocalInspectionMode.current) {
//        LaunchedEffect(Unit) {
//            val feedBytes = Res.readBytes("files/ars_sample.xml")
//            val feedString = feedBytes.decodeToString()
//            val rssParser = provideRssParser()
//            val rssChannel = rssParser.parse(feedString)
//            val channel = Channel.fromRssChannel(rssChannel)
//            channels = listOf(channel)
//        }
//    }

    val navController: NavHostController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        LocalChannelStore provides ChannelStore(provideRssParser()),
        LocalSearchApi provides provideSearchApi(),
        LocalNavHostController provides navController,
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        MaterialTheme {
            Scaffold(
                topBar = { AppBar() },
                bottomBar = { AppBottomBar() },
                snackbarHost = { SnackbarHost(hostState = LocalSnackbarHostState.current) },
                content = { innerPadding ->
                    AppRouter(Modifier.padding(innerPadding))
                }
            )
        }
    }
}

var LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No CompositionLocal LocalSnackbarHostState")
}

var LocalNavHostController = staticCompositionLocalOf<NavHostController> {
    error("No LocalComposition LocalNavHostController")
}