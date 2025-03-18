package the.autarch.android.newsgrid.channel.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import the.autarch.android.newsgrid.LocalNavHostController
import the.autarch.android.newsgrid.channel.data.LocalChannelStore

@Composable
fun ChannelsScreen() {

    val channels by LocalChannelStore.current.channels.collectAsStateWithLifecycle()

    Column {
        LazyColumn {
            items(channels) {
                ChannelItem(it)
            }
        }
    }
}