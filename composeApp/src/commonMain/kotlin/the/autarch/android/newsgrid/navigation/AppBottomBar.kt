package the.autarch.android.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import the.autarch.android.newsgrid.LocalNavHostController

@Composable
fun AppBottomBar(bookmarkIds: List<String>) {

    val navBackStackEntry by LocalNavHostController.current.currentBackStackEntryAsState()

    navBackStackEntry?.let { navBack ->

        if (navBack.destination.hasRoute(Route.EntryDetails::class)) {
            val route = navBackStackEntry!!.toRoute<Route.EntryDetails>()
            val isBookmarked = bookmarkIds.contains(route.entryId)
            EntryDetailBottomBar(route.entryId, isBookmarked)
        }
    }
}