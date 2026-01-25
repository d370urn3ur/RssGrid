package the.autarch.newsgrid

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.cormorant_garamond_variable
import newsgrid.composeapp.generated.resources.jost_italic_variable
import newsgrid.composeapp.generated.resources.jost_variable
import org.jetbrains.compose.resources.Font

@Composable
fun AppTypography() = Typography().run {

    val jb = jostBold()
    val j = jostRegular()
    val ji = jostRegularItalic()
    val cg = cormorantGaramondRegular()

    copy(
        titleLarge = titleLarge.copy(fontFamily = jb),
        titleMedium = titleMedium.copy(fontFamily = jb),
        titleSmall = titleSmall.copy(fontFamily = j),
        bodyLarge = bodyLarge.copy(fontFamily = j),
        bodyMedium = bodyMedium.copy(fontFamily = j),
        bodySmall = bodySmall.copy(fontFamily = j),
        labelLarge = labelLarge.copy(fontFamily = j),
        labelMedium = labelMedium.copy(fontFamily = j),
        labelSmall = labelSmall.copy(fontFamily = j),
    )
}

@Composable
fun cormorantGaramondRegular() = FontFamily(
    Font(
        Res.font.cormorant_garamond_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)

@Composable
fun jostRegular() = FontFamily(
    Font(
        Res.font.jost_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)

@Composable
fun jostBold() = FontFamily(
    Font(
        Res.font.jost_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(800)
        )
    )
)

@Composable
fun jostRegularItalic() = FontFamily(
    Font(
        Res.font.jost_italic_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)