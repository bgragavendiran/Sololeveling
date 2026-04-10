package com.heptre.sololeveling.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modifier to apply a neon glow effect to a composable.
 * @param color The color of the glow (typically SystemBlue or PenaltyRed)
 * @param radius The radius of the glow effect
 */
fun Modifier.neonGlow(color: Color, radius: Dp = 20.dp): Modifier = this.then(
    drawBehind {
        val paint = Paint().asPaint().apply {
            color = color
            isAntiAlias = true
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 0f
            maskFilter = android.graphics.BlurMaskFilter.createBlur(
                android.graphics.BlurMaskFilter.Blur.NORMAL,
                radius.toPx()
            )
        }
        
        // Draw a rectangle with the glow effect
        drawIntoCanvas { canvas ->
            val paintNative = paint.asFrameworkPaint()
            canvas.nativeCanvas.drawRect(
                0f,
                0f,
                size.width,
                size.height,
                paintNative
            )
        }
    }
)

/**
 * Modifier to apply sharp borders without rounded corners.
 * @param color The color of the border
 * @param width The width of the border
 */
fun Modifier.sharpBorder(color: Color, width: Dp = 1.dp): Modifier = this.then(
    Modifier.border(
        width = width,
        color = color,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
    )
)

/**
 * Modifier to apply a deep, dark background with a subtle shadow.
 * @param color The base color of the background
 */
fun Modifier.deepVoidBackground(color: Color = VoidBlack): Modifier = this.then(
    Modifier.background(color)
)