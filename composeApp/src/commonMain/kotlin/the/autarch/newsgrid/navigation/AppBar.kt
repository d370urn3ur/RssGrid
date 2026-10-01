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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation3.runtime.NavKey
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.ic_add
import newsgrid.composeapp.generated.resources.ic_arrow_back
import newsgrid.composeapp.generated.resources.ic_check_circle
import newsgrid.composeapp.generated.resources.ic_cross_circle
import newsgrid.composeapp.generated.resources.ic_delete
import newsgrid.composeapp.generated.resources.ic_reorder
import newsgrid.composeapp.generated.resources.ic_vertical_align_bottom
import org.jetbrains.compose.resources.painterResource
import the.autarch.newsgrid.AppUiAction
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.presentation.ImportChannelFromUrlDialog

// TODO: expect AppBar for each platform (ex: CenterAppBar for iOS)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentlyVisibleRoute: NavKey,
    selectedChannels: List<ChannelEntity>,
    isReordering: Boolean,
    addChannelOperation: TaskProgress<String>,
    onAppAction: (AppUiAction) -> Unit,
    onBack: () -> Unit,
    onNavigateToRoute: (Route) -> Unit
) {

    val isContextual = selectedChannels.isNotEmpty()
    val containerColor = if (isContextual) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
    val contentColor = if (isContextual) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer

    TopAppBar(
        title = { AppBarTitle(currentlyVisibleRoute, isContextual) },
        navigationIcon = { AppBarNavigationIcon(
            currentlyVisibleRoute,
            isContextual,
            { onAppAction(AppUiAction.DeselectChannels) },
            onBack
        ) },
        actions = {
            AppBarActions(
                currentlyVisibleRoute,
                isContextual,
                isReordering,
                { onAppAction(AppUiAction.ToggleReorderingMode) },
                onDelete = {
                    val selectedChannels = selectedChannels
                    onAppAction(AppUiAction.DeselectChannels)
                    onAppAction(AppUiAction.DeleteChannels(selectedChannels))
                },
                addChannelOperation = addChannelOperation,
                onAddChannel = { url -> onAppAction(AppUiAction.AddChannel(url)) },
                onNavigateToRoute = onNavigateToRoute
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
    )
}

@Composable
fun AppBarTitle(currentlyVisibleRoute: NavKey, isContextual: Boolean) {
    when (currentlyVisibleRoute) {
        is Route.Main -> {
            Text(
                if (isContextual) "Edit Channels" else "RssGrid",
                fontWeight = FontWeight.Black
            )
        }
        is Route.Search -> {
            Text(
                "Add a Channel",
                fontWeight = FontWeight.Black
            )
        }
        is Route.EntryDetails -> {
            Text(
                currentlyVisibleRoute.channelTitle,
                fontWeight = FontWeight.Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        is Route.BookmarkDetails -> {
            Text(
                currentlyVisibleRoute.channelTitle,
                fontWeight = FontWeight.Black,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        else -> {}
    }
}

@Composable
fun AppBarActions(
    currentlyVisibleRoute: NavKey,
    isContextual: Boolean,
    isReordering: Boolean,
    onToggleReorder: () -> Unit,
    onDelete: () -> Unit,
    addChannelOperation: TaskProgress<String>,
    onAddChannel: (String) -> Unit,
    onNavigateToRoute: (Route) -> Unit
) {

    var showImportDialog by remember { mutableStateOf(false) }

    when (currentlyVisibleRoute) {
        is Route.Main -> {
            if (isContextual) {
                IconButton({ onDelete() }) {
                    Icon(
                        painterResource(Res.drawable.ic_delete),
                        contentDescription = "Delete selected channels"
                    )
                }
            } else {
                if (currentlyVisibleRoute.tab == TabIndex.CHANNELS) {
                    IconButton(onToggleReorder) {
                        Icon(
                            painter = painterResource(
                                if (isReordering)
                                    Res.drawable.ic_check_circle
                                else
                                    Res.drawable.ic_reorder
                            ),
                            contentDescription = if (isReordering) "Done reordering" else "Reorder channels"
                        )
                    }
                }
                if (!isReordering) {
                    IconButton({ onNavigateToRoute(Route.Search) }) {
                        Icon(
                            painterResource(Res.drawable.ic_add),
                            contentDescription = "Add Channel"
                        )
                    }
                }
            }
        }
        is Route.Search -> IconButton({ showImportDialog = true }) {
            Icon(
                painter = painterResource(Res.drawable.ic_vertical_align_bottom),
                contentDescription = "Import from URL"
            )
        }
        else -> {}
    }

    if (showImportDialog) {
        ImportChannelFromUrlDialog(
            addChannelOperation = addChannelOperation,
            onAddChannel = { url ->
                showImportDialog = false
                onAddChannel(url)
            },
            onDismiss = {
                showImportDialog = false
            }
        )
    }
}

@Composable
fun AppBarNavigationIcon(currentlyVisibleRoute: NavKey, isContextual: Boolean, onDeselectChannels: () -> Unit, onBack: () -> Unit) {

    when (currentlyVisibleRoute) {

        is Route.Main -> if (isContextual) {
            IconButton({ onDeselectChannels() }) {
                Icon(painterResource(Res.drawable.ic_cross_circle), contentDescription = "")
            }
        }
        is Route.Search, is Route.EntryDetails, is Route.BookmarkDetails ->
            IconButton(onBack) {
                Icon(
                    painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = "Navigate Back"
                )
            }
            else -> {}
    }
}