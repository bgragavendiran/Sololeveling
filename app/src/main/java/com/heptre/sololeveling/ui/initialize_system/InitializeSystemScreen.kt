package com.heptre.sololeveling.ui.initialize_system

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*

@Composable
fun InitializeSystemScreen(onInitializeComplete: () -> Unit) {
    var playerName by remember { mutableStateOf("") }
    
    // Radial glow
    val radialBrush = Brush.radialGradient(
        colors = listOf(SystemBlue.copy(alpha = 0.15f), Color.Transparent),
        radius = 800f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .background(radialBrush)
    ) {
        // Decorative corner pieces
        Box(modifier = Modifier.align(Alignment.TopStart).padding(24.dp).size(24.dp).border(width = 2.dp, color = SystemBlue.copy(alpha = 0.3f), shape = RoundedCornerShape(0.dp)))
        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp).size(24.dp).border(width = 2.dp, color = SystemBlue.copy(alpha = 0.3f), shape = RoundedCornerShape(0.dp)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text("PROTOCOL: BOOT_SEQUENCE", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "INITIATING SYSTEM\nPLAYER IDENTIFICATION",
                color = FrostWhite,
                fontSize = 32.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.neonGlow(SystemBlue, 10.dp),
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Input Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Subject_Designation", color = SystemBlue, fontSize = 12.sp, fontFamily = ShareTechMono)
                Spacer(modifier = Modifier.height(8.dp))
                BasicTextField(
                    value = playerName,
                    onValueChange = { playerName = it.uppercase() },
                    textStyle = TextStyle(
                        color = FrostWhite,
                        fontSize = 32.sp,
                        fontFamily = Rajdhani,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(SystemBlue),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehindBottomBorder(2.dp, PrimaryContainer)
                                .padding(vertical = 8.dp)
                        ) {
                            if (playerName.isEmpty()) {
                                Text("ENTER NAME...", color = Slate, fontSize = 32.sp, fontFamily = Rajdhani, letterSpacing = 2.sp)
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Fake Keypad Grid
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for(i in 1..3) {
                    Box(modifier = Modifier.size(12.dp).background(Obsidian).border(1.dp, Color(0xFF1A2235)))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(12.dp).background(Obsidian).border(1.dp, Color(0xFF1A2235)))
                Box(modifier = Modifier.size(12.dp).background(SystemBlue).neonGlow(SystemBlue))
                Box(modifier = Modifier.size(12.dp).background(Obsidian).border(1.dp, Color(0xFF1A2235)))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { if(playerName.isNotBlank()) onInitializeComplete() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(ClippedCornerShape(30f))
                    .neonGlow(SystemBlue, 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SystemBlue, contentColor = VoidBlack)
            ) {
                Text("INITIALIZE SYSTEM", fontSize = 18.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Obsidian)) {
                    Box(modifier = Modifier.fillMaxWidth(0.67f).fillMaxHeight().background(SystemBlue))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("System Sovereign v4.2.0", color = Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
            }
        }
    }
}

// Helper specific to Initialize screen
fun Modifier.drawBehindBottomBorder(strokeWidth: androidx.compose.ui.unit.Dp, color: Color) = this.then(
    this.drawBehind {
        val width = size.width
        val height = size.height - strokeWidth.toPx() / 2
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, height),
            end = androidx.compose.ui.geometry.Offset(width, height),
            strokeWidth = strokeWidth.toPx()
        )
    }
)
