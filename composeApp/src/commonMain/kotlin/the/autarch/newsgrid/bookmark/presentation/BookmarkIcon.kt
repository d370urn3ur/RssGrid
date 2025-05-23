package the.autarch.newsgrid.bookmark.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.ic_bookmark
import org.jetbrains.compose.resources.painterResource

@Composable
fun BookmarkIcon(modifier: Modifier = Modifier.Companion) {
    Icon(
        painterResource(Res.drawable.ic_bookmark),
        contentDescription = "",
        modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        tint = MaterialTheme.colorScheme.onSurface
    )
}