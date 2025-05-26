package the.autarch.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import the.autarch.newsgrid.LocalNavHostController

@Composable
fun AppBottomBar(bookmarkIds: List<String>) {

    val navBackStackEntry by LocalNavHostController.current.currentBackStackEntryAsState()

    navBackStackEntry?.let { navBack ->

        when {
            navBack.destination.hasRoute(Route.EntryDetails::class) == true -> {
                val route = navBackStackEntry!!.toRoute<Route.EntryDetails>()
                val isBookmarked = bookmarkIds.contains(route.entryId)
                EntryDetailBottomBar(route.entryId, isBookmarked)
            }
            navBack.destination.hasRoute(Route.BookmarkDetails::class) == true -> {
                val route = navBackStackEntry!!.toRoute<Route.BookmarkDetails>()
                val isBookmarked = bookmarkIds.contains(route.bookmarkId)
                EntryDetailBottomBar(route.bookmarkId, isBookmarked)
            }
        }
    }
}