package com.heptre.sololeveling.ui.historical_records

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*

@Composable
fun HistoricalRecordsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Text("EVOLUTION LOG // REVISION 4.08", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("HISTORICAL DATA", color = FrostWhite, fontSize = 32.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.neonGlow(SystemBlue.copy(alpha=0.3f), 5.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TabButton("WEEKLY", false)
            TabButton("MONTHLY", true)
            TabButton("YEARLY", false)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Left Column (Charts)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .hudGlass()
                .border(2.dp, SystemBlue)
                .padding(16.dp)
        ) {
            Text("GROWTH VECTOR ANALYSIS", color = FrostWhite, fontSize = 16.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stepped Chart Stub
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    
                    // Grid lines
                    for (i in 1..4) {
                        drawLine(color = Color(0xFF1A2235), start = androidx.compose.ui.geometry.Offset(0f, height * (i/5f)), end = androidx.compose.ui.geometry.Offset(width, height * (i/5f)))
                    }
                    
                    val path = Path().apply {
                        moveTo(0f, height)
                        lineTo(0f, height * 0.8f)
                        lineTo(width * 0.2f, height * 0.8f)
                        lineTo(width * 0.2f, height * 0.6f)
                        lineTo(width * 0.4f, height * 0.6f)
                        lineTo(width * 0.4f, height * 0.4f)
                        lineTo(width * 0.7f, height * 0.4f)
                        lineTo(width * 0.7f, height * 0.2f)
                        lineTo(width, height * 0.2f)
                    }
                    drawPath(path, color = SystemBlue, style = Stroke(width = 4.dp.toPx()))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mini Stats
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            MiniStatCard("PEAK ENDURANCE", "142", "+12.4%", TertiaryRecovery, Modifier.weight(1f))
            MiniStatCard("SYNC PROBABILITY", "88.4", "STABLE", SystemBlue, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Radar Chart Stub
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ClippedCornerShape(20f))
                .background(Obsidian)
                .border(1.dp, SystemBlue.copy(alpha=0.3f), ClippedCornerShape(20f))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("APTITUDE GEOMETRY", color = SystemBlue.copy(alpha=0.5f), fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(modifier = Modifier.height(32.dp))
                // Radar Polygon Stub
                Canvas(modifier = Modifier.size(150.dp).neonGlow(SystemBlue.copy(alpha=0.5f), 10.dp)) {
                    val radius = size.width / 2f
                    val center = androidx.compose.ui.geometry.Offset(radius, radius)
                    
                    // Draw Pentagon
                    val path = Path().apply {
                        moveTo(center.x, 0f)
                        lineTo(size.width, size.height * 0.38f)
                        lineTo(size.width * 0.82f, size.height)
                        lineTo(size.width * 0.18f, size.height)
                        lineTo(0f, size.height * 0.38f)
                        close()
                    }
                    drawPath(path, color = SystemBlue.copy(alpha=0.2f))
                    drawPath(path, color = SystemBlue, style = Stroke(width = 2.dp.toPx()))
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("BALANCE COEFFICIENT", color = Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
                    Text("0.892", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // System Notifications
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("SYSTEM NOTIFICATIONS", color = SystemBlue, fontSize = 12.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.border(width = 2.dp, color = SystemBlue).padding(start = 4.dp))
            Spacer(modifier = Modifier.height(8.dp))
            
            NotificationRow("[UPDATE] Intelligence +1", "Achieved after 'Night Walker' hidden quest.", TertiaryRecovery)
            Spacer(modifier = Modifier.height(8.dp))
            NotificationRow("[LOG] Strength Threshold", "Current power output exceeds previous peak.", SystemBlue)
        }
        
        Spacer(modifier = Modifier.height(80.dp)) // padding for bottom nav
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .background(if (isSelected) SystemBlue.copy(alpha=0.1f) else SurfaceContainerHighest)
            .border(width = 2.dp, color = if(isSelected) SystemBlue else Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(text, color = if(isSelected) SystemBlue else Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
    }
}

@Composable
fun MiniStatCard(title: String, value: String, subValue: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SurfaceContainerHighest)
            .border(width = 1.dp, color = Color(0xFF1A2235))
            .border(width = 2.dp, color = color.copy(alpha=0.3f))
            .padding(16.dp)
    ) {
        Text(title, color = color.copy(alpha=0.6f), fontSize = 10.sp, fontFamily = ShareTechMono)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(value, color = FrostWhite, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
            Text(subValue, color = color, fontSize = 10.sp, fontFamily = ShareTechMono, modifier = Modifier.padding(bottom=4.dp))
        }
    }
}

@Composable
fun NotificationRow(title: String, desc: String, color: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest)
            .drawBehind {
                drawRect(
                    color = color.copy(alpha=0.4f),
                    topLeft = androidx.compose.ui.geometry.Offset(size.width - 2.dp.toPx(), 0f),
                    size = androidx.compose.ui.geometry.Size(2.dp.toPx(), size.height)
                )
            }
            .padding(12.dp)
    ) {
        Text(title, color = color, fontSize = 10.sp, fontFamily = ShareTechMono)
        Spacer(modifier = Modifier.height(2.dp))
        Text(desc, color = Slate, fontSize = 11.sp, fontFamily = Outfit)
    }
}
