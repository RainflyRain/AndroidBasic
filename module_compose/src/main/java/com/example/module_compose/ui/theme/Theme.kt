package com.example.module_compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ComposeColors(
    val navigation: Color = Color.Unspecified,
    val dailyCalories: Color = Color.Unspecified,
    val dailyExerciseTime: Color = Color.Unspecified,
    val gradientBg0:Color = Color.Unspecified,
    val gradientBg1:Color = Color.Unspecified,

    val textPrimary:Color = Color.Unspecified,
    val textSecondary:Color = Color.Unspecified,
    val textTertiary:Color = Color.Unspecified,

    val line:Color = Color.Unspecified
)

private val LocalExtendedColors = staticCompositionLocalOf { ComposeColors() }

private val DarkColors = darkColors(
    primary = Primary,
    secondary = Secondary,
    background = Background
    // ...
)
private val LightColors = lightColors(
    primary = Primary,
    secondary = Secondary,
    background = Background
    // ...
)


@Composable
fun ComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors = ComposeColors(
        navigation = Navigation,
        dailyCalories = DailyCalories,
        dailyExerciseTime = DailyExerciseTime,
        gradientBg0 = GradientBg0,
        gradientBg1 = GradientBg1,
        textPrimary = TextPrimary,
        textSecondary = TextSecondary,
        textTertiary = TextTertiary,
        line = Line
    )

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colors = if (darkTheme) DarkColors else LightColors,
            content = content
        )
    }
}

// Use with eg. ExtendedTheme.colors.tertiary
object ComposeTheme {
    val colors: ComposeColors
        @Composable
        get() = LocalExtendedColors.current
}