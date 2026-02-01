package the.autarch.newsgrid

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import newsgrid.composeapp.generated.resources.Res
import newsgrid.composeapp.generated.resources.jost_italic_variable
import newsgrid.composeapp.generated.resources.jost_variable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

//val jb = jostBold()
//val j = jostRegular()
//val ji = jostRegularItalic()
//val cg = cormorantGaramondRegular()

@OptIn(ExperimentalResourceApi::class)
@Composable
fun jostFamily() = FontFamily(
    Font(
        Res.font.jost_variable,
        weight = FontWeight.Thin,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Thin.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.Thin,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Thin.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.ExtraLight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.ExtraLight.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.ExtraLight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.ExtraLight.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.Light,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Light.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.Light,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Light.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Normal.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Normal.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Medium.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.SemiBold.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.SemiBold.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Bold.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Bold.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.ExtraBold.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.ExtraBold.weight)
        )
    ),
    Font(
        Res.font.jost_variable,
        weight = FontWeight.Black,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Black.weight)
        )
    ),
    Font(
        Res.font.jost_italic_variable,
        weight = FontWeight.Black,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(FontWeight.Black.weight)
        )
    ),
)

@Suppress("ComposableNaming")
@Composable
fun AppTypography() = Typography().run {

    val fontFamily = jostFamily()

    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily =  fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}

//@Composable
//fun cormorantGaramondRegular() = FontFamily(
//    Font(
//        Res.font.cormorant_garamond_variable,
//        variationSettings = FontVariation.Settings(
//            FontVariation.weight(400)
//        )
//    )
//)
//
//@Composable
//fun jostRegular() = FontFamily(
//    Font(
//        Res.font.jost_variable,
//        variationSettings = FontVariation.Settings(
//            FontVariation.weight(400)
//        )
//    )
//)
//
//@Composable
//fun jostBold() = FontFamily(
//    Font(
//        Res.font.jost_variable,
//        variationSettings = FontVariation.Settings(
//            FontVariation.weight(800)
//        )
//    )
//)
//
//@Composable
//fun jostRegularItalic() = FontFamily(
//    Font(
//        Res.font.jost_italic_variable,
//        variationSettings = FontVariation.Settings(
//            FontVariation.weight(400)
//        )
//    )
//)