package the.autarch.newsgrid

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.bookmark.data.BookmarkSummary
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.navigation.AppBar
import the.autarch.newsgrid.navigation.AppBottomBar
import the.autarch.newsgrid.navigation.AppRouter
import the.autarch.newsgrid.navigation.Route
import the.autarch.newsgrid.navigation.navConfig

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
    val container = remember(builder) { DIContainer(dataStore, database) }

    val colorScheme = rememberColorScheme()
    val snackbarHostState = remember { SnackbarHostState() }
    val backStack = rememberNavBackStack(navConfig, Route.Main())

    val viewModel: AppViewModel = appViewModel(container)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.addChannelOperation) {
        when (val status = uiState.addChannelOperation) {
            is TaskProgress.Success -> {
                snackbarHostState.showSnackbar("Added channel: ${status.result}")
                viewModel.onAction(AppUiAction.OnAddChannelHandled)
            }
            is TaskProgress.Failure -> {
                snackbarHostState.showSnackbar("Error adding channel: ${status.error.message}", duration = SnackbarDuration.Indefinite)
                viewModel.onAction(AppUiAction.OnAddChannelHandled)
            }
            else -> {}
        }
    }

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
        LocalDIContainer provides container
    ) {

        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography()
        ) {
            Scaffold(
                topBar = {
                    AppBar(
                        currentlyVisibleRoute = backStack.last(),
                        selectedChannels = uiState.selectedChannels,
                        isReordering = uiState.isReordering,
                        addChannelOperation = uiState.addChannelOperation,
                        onAppAction = { viewModel.onAction(it) },
                        onBack = { backStack.removeLastOrNull() },
                        onNavigateToRoute = { backStack.add(it) }
                    )
                },
                bottomBar = {
                    AppBottomBar(
                        currentlyVisibleRoute = backStack.last(),
                        bookmarkIds = uiState.bookmarks.map { it.link },
                        onAppAction = { viewModel.onAction(it) }
                    )
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = LocalSnackbarHostState.current,
                        modifier = Modifier.imePadding()
                    )
                },
                content = { innerPadding ->
                    AppRouter(
                        backStack,
                        Modifier.padding(innerPadding),
                        uiState.selectedChannels,
                        isReordering = uiState.isReordering,
                        addChannelOperation = uiState.addChannelOperation,
                        onAppAction = { viewModel.onAction(it) },
                        bookmarks = uiState.bookmarks
                    )
                }
            )
        }
    }
}

@Stable
data class AppUiState(
    val selectedChannels: List<ChannelEntity> = emptyList(),
    val isReordering: Boolean = false,
    val bookmarks: List<BookmarkSummary> = emptyList(),
    val addChannelOperation: TaskProgress<String> = TaskProgress.Idle
)

sealed interface AppUiAction {
    data class AddChannel(val url: String): AppUiAction
    data class DeleteChannels(val channels: List<ChannelEntity>): AppUiAction
    data object OnAddChannelHandled: AppUiAction
    data object RefreshChannels: AppUiAction
    data class AddBookmark(val entryId: String): AppUiAction
    data class RemoveBookmark(val entryId: String): AppUiAction
    data class ToggleChannelSelected(val channel: ChannelEntity): AppUiAction
    data object DeselectChannels: AppUiAction
    data object ToggleReorderingMode: AppUiAction
}

var LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("No CompositionLocal LocalSnackbarHostState")
}