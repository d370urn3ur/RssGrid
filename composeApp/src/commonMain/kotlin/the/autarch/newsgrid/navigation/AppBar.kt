package the.autarch.newsgrid.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.ic_add
import newsgrid.composeapp.generated.resources.ic_arrow_back
import newsgrid.composeapp.generated.resources.ic_cross_circle
import newsgrid.composeapp.generated.resources.ic_delete
import org.jetbrains.compose.resources.painterResource
import the.autarch.newsgrid.LocalNavHostController
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.data.LocalChannelStore

// TODO: expect AppBar for each platform (ex: CenterAppBar for iOS)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(selectedChannels: List<ChannelEntity>, onDeselectChannels: () -> Unit) {

    val navBackStackEntry by LocalNavHostController.current.currentBackStackEntryAsState()
    val isContextual = selectedChannels.isNotEmpty()
    val containerColor = if (isContextual) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    val titleContentColor = if (isContextual) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary

    val scope = rememberCoroutineScope()
    val store = LocalChannelStore.current

    TopAppBar(
        title = { AppBarTitle(navBackStackEntry, isContextual) },
        navigationIcon = { AppBarNavigationIcon(navBackStackEntry, isContextual, onDeselectChannels) },
        actions = {
            AppBarActions(navBackStackEntry, isContextual) { scope.launch {
                onDeselectChannels()
                store.deleteChannels(selectedChannels)
            } }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = titleContentColor,
        ),
    )
}

@Composable
fun AppBarTitle(navBackStackEntry: NavBackStackEntry?, isContextual: Boolean) {
    navBackStackEntry?.let { navBack ->
        navBack.destination.let {
            when {
                it.hasRoute(Route.AppContainer::class) -> {
                    if (isContextual) {
                        Text("Edit Channels")
                    } else {
                        Text("NewsGrid")
                    }
                }
                it.hasRoute(Route.Search::class) -> Text("Add a Channel")
                it.hasRoute(Route.EntryDetails::class) -> {
                    val route = navBack.toRoute<Route.EntryDetails>()
                    Text(route.channelTitle)
                }
            }
        }
    }
}

@Composable
fun AppBarActions(navBackStackEntry: NavBackStackEntry?, isContextual: Boolean, onDelete: () -> Unit) {

    val navController = LocalNavHostController.current

    navBackStackEntry?.let { navBack ->
        navBack.destination.let {
            when {
                it.hasRoute(Route.AppContainer::class) && isContextual -> {
                    IconButton({ onDelete() }) {
                        Icon(painterResource(Res.drawable.ic_delete), contentDescription = "Delete selected channels")
                    }
                }
                it.hasRoute(Route.AppContainer::class) -> IconButton({ navController.navigate(Route.Search) }) {
                    Icon(painterResource(Res.drawable.ic_add), contentDescription = "Add Channel")
                }
                it.hasRoute(Route.Search::class) -> AppBarButtonImportChannel()
            }
        }
    }
}

@Composable
fun AppBarNavigationIcon(navBackStackEntry: NavBackStackEntry?, isContextual: Boolean, onDeselectChannels: () -> Unit) {

    val navController = LocalNavHostController.current

    navBackStackEntry?.let { navBack ->
        navBack.destination.let {
            when {
                it.hasRoute(Route.AppContainer::class) && isContextual ->
                    IconButton({ onDeselectChannels() }) {
                        Icon(painterResource(Res.drawable.ic_cross_circle), contentDescription = "")
                    }
                it.hasRoute(Route.Search::class) || it.hasRoute(Route.EntryDetails::class) ->
                    IconButton({ navController.popBackStack() }) {
                        Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = "Navigate Back")
                    }
            }
        }
    }
}