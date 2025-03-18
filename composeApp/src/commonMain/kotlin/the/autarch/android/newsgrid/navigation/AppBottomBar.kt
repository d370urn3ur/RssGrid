package the.autarch.android.newsgrid.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.bookmark_border
import org.jetbrains.compose.resources.painterResource
import the.autarch.android.newsgrid.LocalNavHostController

@Composable
fun AppBottomBar() {

    val navBackStackEntry by LocalNavHostController.current.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route
    val root = route?.split("/")?.firstOrNull()

    when (root) {
        Routes.EntryDetails::class.qualifiedName ->
            BottomAppBar(
                actions = {
                    IconButton({  }) {
                        Icon(
                            painterResource(Res.drawable.bookmark_border),
                            contentDescription = "Save entry to bookmarks"
                        )
                    }
                }
            )
    }
}