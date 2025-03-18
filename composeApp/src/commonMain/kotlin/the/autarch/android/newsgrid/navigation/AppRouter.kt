package the.autarch.android.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import the.autarch.android.newsgrid.AppContainer
import the.autarch.android.newsgrid.LocalNavHostController
import the.autarch.android.newsgrid.entry.data.Entry
import the.autarch.android.newsgrid.entry.data.fromParcelized
import the.autarch.android.newsgrid.entry.presentation.EntryDetailsScreen
import the.autarch.android.newsgrid.search.presentation.SearchScreen

@Composable
fun AppRouter(modifier: Modifier = Modifier) {

    NavHost(
        navController = LocalNavHostController.current,
        startDestination = Routes.AppContainer,
        modifier = modifier,
    ) {
        composable<Routes.AppContainer> {
            AppContainer()
        }
        composable<Routes.Search> {
            SearchScreen()
        }
        composable<Routes.EntryDetails> { backStackEntry ->
            val detailRoute = backStackEntry.toRoute<Routes.EntryDetails>()
            EntryDetailsScreen(detailRoute.entry)
        }
    }
}