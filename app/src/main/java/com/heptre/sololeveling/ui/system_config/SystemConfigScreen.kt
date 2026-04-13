package com.heptre.sololeveling.ui.system_config

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*

@Composable
fun SystemConfigScreen(viewModel: SystemConfigViewModel) {
    val player by viewModel.playerState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(SurfaceContainerHighest).border(1.dp, SystemBlue.copy(alpha=0.3f)))
            Spacer(modifier = Modifier.width(12.dp))
            Text("SYSTEM STATUS", color = SystemBlue, fontSize = 20.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.neonGlow(SystemBlue.copy(alpha=0.5f), 5.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Rank Progression HUD
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .hudGlass()
                .clip(ClippedCornerShape(20f))
                .padding(24.dp)
        ) {
            Text("RANK: ${player?.rank?.name ?: "B"}+", color = SystemBlue, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.neonGlow(SystemBlue, 5.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                val cycleStartDate = player?.cycleStartDate ?: System.currentTimeMillis()
                val daysPassed = ((System.currentTimeMillis() - cycleStartDate) / (1000 * 60 * 60 * 24)).coerceAtLeast(0)
                Text("Evolution Cycle", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                Text("$daysPassed/21 DAYS", color = PrimaryContainer, fontSize = 16.sp, fontFamily = ShareTechMono)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth().height(12.dp)) {
                for(i in 1..3) Box(modifier = Modifier.weight(1f).fillMaxHeight().background(SystemBlue).neonGlow(SystemBlue, 5.dp))
                for(i in 1..2) Box(modifier = Modifier.weight(1f).fillMaxHeight().background(SurfaceContainerHighest))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Consistency requirement for Rank A threshold: 66% Complete", color = Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Settings / Config items
        ConfigCard(title = "APTITUDE TIMER", icon = "hourglass_empty") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Duration Setting", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                Text("45 MINUTES", color = PrimaryContainer, fontSize = 12.sp, fontFamily = ShareTechMono)
            }
            Spacer(modifier = Modifier.height(8.dp))
            var sliderPosition by remember { mutableFloatStateOf(45f) }
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 15f..120f,
                steps = 6,
                colors = SliderDefaults.colors(
                    thumbColor = SystemBlue,
                    activeTrackColor = SystemBlue,
                    inactiveTrackColor = SurfaceContainerHighest
                )
            )
            Box(modifier = Modifier.background(SurfaceContainerLowest).border(1.dp, Color(0xFF1A2235)).padding(16.dp)) {
                Text("System Aptitude windows determine the intensity of information absorption.", color = Slate, fontSize = 11.sp, fontFamily = Outfit)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // System Toggles
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .hudGlass()
                .border(width = 1.dp, color = SystemBlue.copy(alpha=0.1f))
                .padding(24.dp)
        ) {
            Column {
                Text("SYSTEM PREFERENCES", color = Slate.copy(alpha=0.5f), fontSize = 20.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))
                
                ToggleRow("System Notifications", "Direct neural alerts for Quest spawns.", true)
                Spacer(modifier = Modifier.height(16.dp))
                ToggleRow("Aggressive HUD Overlay", "Force system priority.", false)
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ConfigCard(title: String, icon: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .hudGlass()
            .border(width = 1.dp, color = SystemBlue.copy(alpha=0.2f))
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Placeholder for real icons
            Text(title, color = FrostWhite, fontSize = 18.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        content()
    }
}

@Composable
fun ToggleRow(title: String, desc: String, isOn: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = FrostWhite, fontSize = 16.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
            Text(desc, color = Slate, fontSize = 12.sp, fontFamily = Outfit)
        }
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(24.dp)
                .background(if (isOn) PrimaryContainer else SurfaceContainerHighest)
                .padding(4.dp),
            contentAlignment = if (isOn) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(modifier = Modifier.size(16.dp).background(if(isOn) VoidBlack else Slate))
        }
    }
}
