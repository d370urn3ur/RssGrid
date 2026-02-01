package the.autarch.newsgrid

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import the.autarch.newsgrid.channel.api.provideRssParser
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.data.ChannelStore
import the.autarch.newsgrid.channel.data.LocalChannelStore
import the.autarch.newsgrid.navigation.AppBar
import the.autarch.newsgrid.navigation.AppBottomBar
import the.autarch.newsgrid.navigation.AppRouter
import the.autarch.newsgrid.search.api.LocalSearchApi
import the.autarch.newsgrid.search.api.provideSearchApi

var dataStore: DataStore<Preferences>? = null

@Composable
fun App() {

    val dataStore: DataStore<Preferences> = remember {
        if (dataStore == null) {
            dataStore = createDataStore()
        }
        dataStore!!
    }

    val builder = rememberDatabaseBuilder()
    val database = remember { getRoomDatabase(builder) }

    val colorScheme = rememberColorScheme()

    val navController: NavHostController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedChannels by remember { mutableStateOf<List<ChannelEntity>>(emptyList()) }

    CompositionLocalProvider(
        LocalChannelStore provides ChannelStore(database, provideRssParser(), dataStore),
        LocalSearchApi provides provideSearchApi(),
        LocalNavHostController provides navController,
        LocalSnackbarHostState provides snackbarHostState,
    ) {

        val bookmarks by LocalChannelStore.current.bookmarks.collectAsStateWithLifecycle(emptyList())

        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography()
        ) {
            Scaffold(
                topBar = {
                    AppBar(selectedChannels) {
                        selectedChannels = emptyList()
                    }
                },
                bottomBar = { AppBottomBar(bookmarks.map { it.link }) },
                snackbarHost = { SnackbarHost(hostState = LocalSnackbarHostState.current) },
                content = { innerPadding ->
                    AppRouter(Modifier.padding(innerPadding), selectedChannels) { selectedChannel ->
                        if (selectedChannels.contains(selectedChannel)) {
                            selectedChannels -= selectedChannel
                        } else {
                            selectedChannels += selectedChannel
                        }
                    }
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