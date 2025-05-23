@file:JvmName("ColorProviderAndroid")
package the.autarch.newsgrid

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

fun dynamicColorProvider(isDarkTheme: Boolean, context: Context): ColorScheme? {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    return when {
        dynamicColor && isDarkTheme -> {
            dynamicDarkColorScheme(context)
        }
        dynamicColor && !isDarkTheme -> {
            dynamicLightColorScheme(context)
        }
        else -> null
    }
}

@Composable
actual fun rememberColorScheme(): ColorScheme =
    dynamicColorProvider(isSystemInDarkTheme(), LocalContext.current)?.let {
        remember { it }
    } ?: defaultColorScheme()