package com.spachecor.librosmart.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = Color.White, // Texto sobre primary
    onSecondary = Color.White, // Texto sobre secondary
    onBackground = Color(0xFFE1E1E1), // Texto sobre fondo oscuro
    onSurface = Color(0xFFE1E1E1), // Texto sobre superficie oscura
    error = ErrorDark,
    onError = Color.Black // Texto sobre error
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White, // Texto sobre primary
    onSecondary = Color.Black, // Texto sobre secondary
    onBackground = Color.Black, // Texto sobre fondo claro
    onSurface = Color.Black, // Texto sobre superficie clara
    error = ErrorLight,
    onError = Color.White // Texto sobre error
)

@Composable
fun LibroSmartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    typography: FontFamily,
    isSquareTheme: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Determina si usar esquinas cuadradas o redondeadas
    val shapes = if (isSquareTheme) {
        Shapes(
            small = RoundedCornerShape(0.dp),  // Bordes cuadrados pequeños
            medium = RoundedCornerShape(0.dp), // Bordes cuadrados medianos
            large = RoundedCornerShape(0.dp)   // Bordes cuadrados grandes
        )
    } else {
        Shapes(
            small = RoundedCornerShape(16.dp),  // Bordes redondeados pequeños
            medium = RoundedCornerShape(4.dp), // Bordes redondeados medianos
            large = RoundedCornerShape(0.dp)   // Bordes rectos grandes
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography.copy(
            bodyLarge = Typography.bodyLarge.copy(
                fontFamily = typography
            )
        ),
        shapes = shapes,
        content = content
    )
}