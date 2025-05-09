package the.autarch.android.newsgrid.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import the.autarch.android.newsgrid.LocalNavHostController

// TODO: expect AppBar for each platform (ex: CenterAppBar for iOS)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {

    val navBackStackEntry by LocalNavHostController.current.currentBackStackEntryAsState()

    TopAppBar(
        title = { AppBarTitle(navBackStackEntry) },
        navigationIcon = { AppBarNavigationIcon(navBackStackEntry?.destination?.route) },
        actions = { AppBarActions(navBackStackEntry?.destination?.route) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Composable
fun AppBarTitle(navBackStackEntry: NavBackStackEntry?) {

    val route = navBackStackEntry?.destination?.route
    val root = route?.split("/")?.firstOrNull()

    when (root) {
        Route.AppContainer::class.qualifiedName -> Text("NewsGrid")
        Route.Search::class.qualifiedName -> Text("Add a Channel")
        Route.EntryDetails::class.qualifiedName -> {
            navBackStackEntry?.toRoute<Route.EntryDetails>()?.let {
                Text(it.channelTitle)
            } ?: Text("Unknown channel")
        }
    }
}

@Composable
fun AppBarActions(route: String?) {

    val navController = LocalNavHostController.current

    when (route) {
        Route.AppContainer::class.qualifiedName -> IconButton({ navController.navigate(Route.Search) }) {
            Icon(Icons.Default.Add, contentDescription = "Add Channel")
        }
        Route.Search::class.qualifiedName -> AppBarButtonImportChannel()
    }
}

@Composable
fun AppBarNavigationIcon(route: String?) {

    val root = route?.split("/")?.firstOrNull()
    val navController = LocalNavHostController.current

    when (root) {
        Route.Search::class.qualifiedName,
        Route.EntryDetails::class.qualifiedName ->
            IconButton({ navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Navigate Back")
            }
    }
}