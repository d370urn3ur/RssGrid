package the.autarch.newsgrid

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicColorScheme

@Composable
expect fun rememberColorScheme(): ColorScheme

@Composable
fun defaultColorScheme(): ColorScheme = rememberDynamicColorScheme(
    seedColor = Color(0xFFA1FF00),
    isDark = isSystemInDarkTheme(),
    style = PaletteStyle.Vibrant
)