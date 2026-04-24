package com.heptre.sololeveling.ui.system_config

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun SystemConfigScreen(viewModel: SystemConfigViewModel) {
    val player by viewModel.playerState.collectAsState()
    val aptitudeMinutes by viewModel.aptitudeTimerMinutes.collectAsState()
    val notificationsOn by viewModel.notificationsEnabled.collectAsState()
    val aggressiveHudOn by viewModel.aggressiveHudEnabled.collectAsState()

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
            Box(modifier = Modifier.size(40.dp).background(SurfaceContainerHighest).border(1.dp, SystemBlue.copy(alpha = 0.3f)))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "SYSTEM STATUS",
                color = SystemBlue,
                fontSize = 20.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.neonGlow(SystemBlue.copy(alpha = 0.5f), 5.dp)
            )
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
            val rankLabel = player?.rank?.name ?: "?"
            Text(
                "RANK: $rankLabel",
                color = SystemBlue,
                fontSize = 24.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.neonGlow(SystemBlue, 5.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                val cycleStartDate = player?.cycleStartDate ?: System.currentTimeMillis()
                val daysPassed = ((System.currentTimeMillis() - cycleStartDate) / (1000 * 60 * 60 * 24)).coerceAtLeast(0)
                val daysLeft = (21 - daysPassed).coerceAtLeast(0)
                Text("Evolution Cycle", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                Text("$daysPassed/21 DAYS", color = PrimaryContainer, fontSize = 16.sp, fontFamily = ShareTechMono)
            }
            Spacer(modifier = Modifier.height(16.dp))
            val cycleStartDate = player?.cycleStartDate ?: System.currentTimeMillis()
            val daysPassed = ((System.currentTimeMillis() - cycleStartDate) / (1000 * 60 * 60 * 24)).coerceIn(0L, 21L)
            val filledSegments = ((daysPassed / 21f) * 5).roundToInt().coerceIn(0, 5)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth().height(12.dp)) {
                for (i in 1..filledSegments) Box(modifier = Modifier.weight(1f).fillMaxHeight().background(SystemBlue).neonGlow(SystemBlue, 5.dp))
                for (i in 1..(5 - filledSegments)) Box(modifier = Modifier.weight(1f).fillMaxHeight().background(SurfaceContainerHighest))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Consistency requirement for next rank threshold: 66%", color = Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Aptitude Timer Config
        ConfigCard(title = "APTITUDE TIMER", icon = "hourglass_empty") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Duration Setting", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                Text("$aptitudeMinutes MINUTES", color = PrimaryContainer, fontSize = 12.sp, fontFamily = ShareTechMono)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = aptitudeMinutes.toFloat(),
                onValueChange = { viewModel.setAptitudeTimer(it.roundToInt()) },
                valueRange = 15f..120f,
                steps = 6,
                colors = SliderDefaults.colors(
                    thumbColor = SystemBlue,
                    activeTrackColor = SystemBlue,
                    inactiveTrackColor = SurfaceContainerHighest
                )
            )
            Box(modifier = Modifier.background(SurfaceContainerLowest).border(1.dp, Color(0xFF1A2235)).padding(16.dp)) {
                Text(
                    "System Aptitude windows determine the intensity of information absorption.",
                    color = Slate,
                    fontSize = 11.sp,
                    fontFamily = Outfit
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // System Toggles
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .hudGlass()
                .border(width = 1.dp, color = SystemBlue.copy(alpha = 0.1f))
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "SYSTEM PREFERENCES",
                    color = Slate.copy(alpha = 0.5f),
                    fontSize = 20.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                ToggleRow(
                    title = "System Notifications",
                    desc = "Direct neural alerts for Quest spawns.",
                    isOn = notificationsOn,
                    onToggle = { viewModel.setNotifications(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ToggleRow(
                    title = "Aggressive HUD Overlay",
                    desc = "Force system priority.",
                    isOn = aggressiveHudOn,
                    onToggle = { viewModel.setAggressiveHud(it) }
                )
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
            .border(width = 1.dp, color = SystemBlue.copy(alpha = 0.2f))
            .padding(24.dp)
    ) {
        Text(title, color = FrostWhite, fontSize = 18.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        content()
    }
}

@Composable
fun ToggleRow(title: String, desc: String, isOn: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = FrostWhite, fontSize = 16.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
            Text(desc, color = Slate, fontSize = 12.sp, fontFamily = Outfit)
        }
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(24.dp)
                .background(if (isOn) PrimaryContainer else SurfaceContainerHighest)
                .clickable { onToggle(!isOn) }
                .padding(4.dp),
            contentAlignment = if (isOn) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(modifier = Modifier.size(16.dp).background(if (isOn) VoidBlack else Slate))
        }
    }
}
