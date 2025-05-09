package the.autarch.android.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import the.autarch.android.newsgrid.AppContainer
import the.autarch.android.newsgrid.LocalNavHostController
import the.autarch.android.newsgrid.entry.presentation.EntryDetailsScreen
import the.autarch.android.newsgrid.search.presentation.SearchScreen

@Composable
fun AppRouter(modifier: Modifier = Modifier) {

    NavHost(
        navController = LocalNavHostController.current,
        startDestination = Route.AppContainer,
        modifier = modifier,
    ) {
        composable<Route.AppContainer> {
            AppContainer()
        }
        composable<Route.Search> {
            SearchScreen()
        }
        composable<Route.EntryDetails> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<Route.EntryDetails>()
            EntryDetailsScreen(detailRoute.entryId)
        }
    }
}