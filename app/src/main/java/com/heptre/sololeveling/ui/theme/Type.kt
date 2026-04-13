package com.heptre.sololeveling.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Custom Typography from PRD
// Using default font families since no font resources are bundled yet.
// Replace with actual font resources (e.g. R.font.rajdhani) when available.
val Rajdhani = FontFamily.Default
val ShareTechMono = FontFamily.Monospace
val Outfit = FontFamily.SansSerif

// Typography styles based on PRD specifications
val Typography = Typography(
    // Headings/Numbers: Rajdhani, 700, 24-48px (Ranks, Timers, Big Stats)
    displayLarge = TextStyle(
        fontFamily = Rajdhani,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Rajdhani,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Rajdhani,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // System Alerts: Share Tech Mono, 400, 12-14px (Metadata, Logs, Timestamps)
    labelLarge = TextStyle(
        fontFamily = ShareTechMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = ShareTechMono,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp
    ),

    // Body: Outfit, 400, 16px (Quest descriptions, readable text)
    bodyLarge = TextStyle(
        fontFamily = Outfit,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Buttons: Rajdhani, 600, 16px, ALL CAPS, 2px letter-spacing
    labelSmall = TextStyle(
        fontFamily = Rajdhani,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 2.sp
    )
)
