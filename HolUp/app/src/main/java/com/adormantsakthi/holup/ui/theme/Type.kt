package com.adormantsakthi.holup.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Italiana,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        color = Color.White,
    ),

    titleMedium = TextStyle(
        fontFamily = Italiana,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        color = Color.White,
    ),

    titleSmall = TextStyle(
        fontFamily = Karma,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = Color.White
    ),

    labelSmall = TextStyle(
        fontFamily = Fresca,
        fontSize = 18.sp,
        color = Color.White
    ),

    labelMedium = TextStyle(
        fontFamily = Fresca,
        fontSize = 24.sp,
        color = Color.Black
    ),

    labelLarge = TextStyle(
        fontFamily = Fresca,
        fontSize = 33.sp,
        color = Color.Black
    ),
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