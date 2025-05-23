package the.autarch.newsgrid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import the.autarch.newsgrid.AppContainer
import the.autarch.newsgrid.LocalNavHostController
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.entry.presentation.EntryDetailsScreen
import the.autarch.newsgrid.search.presentation.SearchScreen

@Composable
fun AppRouter(modifier: Modifier = Modifier, selectedChannels: List<ChannelEntity>, onSelectChannel: (ChannelEntity) -> Unit) {

    NavHost(
        navController = LocalNavHostController.current,
        startDestination = Route.AppContainer,
        modifier = modifier,
    ) {
        composable<Route.AppContainer> {
            AppContainer(selectedChannels, onSelectChannel)
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