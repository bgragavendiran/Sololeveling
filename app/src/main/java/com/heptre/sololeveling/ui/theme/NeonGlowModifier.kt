package com.heptre.sololeveling.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modifier to apply a neon glow effect to a composable.
 */
fun Modifier.neonGlow(color: Color, radius: Dp = 10.dp): Modifier = this.then(
    drawBehind {
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 2f
            maskFilter = BlurMaskFilter(
                radius.toPx(),
                BlurMaskFilter.Blur.OUTER
            )
        }
        
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawRect(
                0f,
                0f,
                size.width,
                size.height,
                paint
            )
        }
    }
)

/**
 * Modifier to apply a deep, dark background with a subtle shadow (glassmorphism simulation).
 */
fun Modifier.hudGlass(): Modifier = this.then(
    Modifier.background(Color(0xFF050507).copy(alpha = 0.6f))
)

/**
 * Modifier for the glitch background used in Penalty screens. 
 * Since pure CSS repeating-linear-gradient is complex, we simulate it with a simple overlay.
 */
fun Modifier.glitchBackground(): Modifier = this.then(
    Modifier.drawWithContent {
        drawContent()
        drawRect(
            color = PenaltyRed.copy(alpha = 0.05f)
        )
    }
)

/**
 * Modifier to apply sharp borders without rounded corners.
 */
fun Modifier.sharpBorder(color: Color, width: Dp = 1.dp): Modifier = this.then(
    Modifier.border(
        width = width,
        color = color,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
    )
)