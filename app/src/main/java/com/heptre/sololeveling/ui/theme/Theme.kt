package com.heptre.sololeveling.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SystemSovereignColorScheme = darkColorScheme(
    primary = SystemBlue,
    onPrimary = VoidBlack,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = FrostWhite,
    
    secondary = ShadowPurple,
    onSecondary = FrostWhite,
    
    tertiary = TertiaryRecovery,
    onTertiary = VoidBlack,
    
    error = PenaltyRed,
    onError = VoidBlack,
    errorContainer = PenaltyRed,
    onErrorContainer = FrostWhite,
    
    background = VoidBlack,
    onBackground = FrostWhite,
    
    surface = Obsidian,
    onSurface = FrostWhite,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = Slate,
    
    outline = Slate
)

@Composable
fun SololevelingTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SystemSovereignColorScheme,
        typography = Typography,
        content = content
    )
}