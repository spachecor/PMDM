package com.spachecor.librosmart.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.spachecor.librosmart.R

val TimesNewRoman = FontFamily(
    Font(R.font.timesnewroman, FontWeight.Normal),
    Font(R.font.timesnewromanbold, FontWeight.Bold),
    Font(R.font.timesnewromanbolditalic, FontWeight.Bold, FontStyle.Italic)
)

val AustieBostKittenKlub = FontFamily(
    Font(R.font.austiebostkittenklub)
)

val Square = FontFamily(
    Font(R.font.square)
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = TimesNewRoman,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)