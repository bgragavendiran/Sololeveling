package com.heptre.sololeveling.ui.status_window

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.data.Player
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.Stats
import com.heptre.sololeveling.ui.theme.*

@Composable
fun StatusWindowScreen(viewModel: StatusWindowViewModel) {
    val player by viewModel.player.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isExpanded by viewModel.isExpanded.collectAsState()
    
    // Handle loading state
    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VoidBlack)
            ) {
                Text(
                    text = "SYSTEM LOADING...",
                    color = SystemBlue,
                    modifier = Modifier.align(Alignment.Center).neonGlow(SystemBlue, 5.dp),
                    fontSize = 18.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else -> {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VoidBlack),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar (Obsidian background, border-bottom #1A2235)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Obsidian)
                        .border(width = 1.dp, color = Color(0xFF1A2235))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${player.name.uppercase()} | TITLE: ${player.title.uppercase()}",
                        color = FrostWhite,
                        fontSize = 16.sp,
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.neonGlow(SystemBlue.copy(alpha = 0.5f), 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Rank Badge Area
                RankBadge(rank = player.rank)

                Spacer(modifier = Modifier.height(32.dp))

                // Stat Bars
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    item {
                        for (statType in listOf(StatType.STRENGTH, StatType.APPTITUDE, StatType.INTELLIGENCE, StatType.ENDURANCE)) {
                            StatBar(
                                statType = statType,
                                value = player.getStatValue(statType),
                                isExpanded = isExpanded,
                                onStatClick = { viewModel.toggleStatExpansion() }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        EarningGoal(gold = player.gold)
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RankBadge(rank: Rank) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .neonGlow(SystemBlue, 20.dp)
                .clip(DiamondShape())
                .background(Obsidian)
                .border(2.dp, SystemBlue, DiamondShape()),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(1.dp, Color(0xFF1A2235), DiamondShape()),
                contentAlignment = Alignment.Center
            ) {
                val rankLetter = if (rank == Rank.UNRANKED) "?" else rank.name
                Text(
                    text = rankLetter,
                    color = SystemBlue,
                    fontSize = 48.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.neonGlow(SystemBlue, 5.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "RANK",
            color = Slate,
            fontSize = 12.sp,
            fontFamily = ShareTechMono,
            letterSpacing = 4.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Status Evaluation: Stable",
            color = FrostWhite.copy(alpha = 0.5f),
            fontSize = 10.sp,
            fontFamily = ShareTechMono
        )
    }
}

@Composable
fun StatBar(
    statType: StatType,
    value: Int,
    isExpanded: Boolean,
    onStatClick: () -> Unit
) {
    val statName = statType.displayName
    val isAnyZero = value == 0
    val textColor = if (isAnyZero) Slate else FrostWhite
    val valueColor = if (isAnyZero) Slate else SystemBlue
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStatClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = statName,
                color = textColor,
                fontSize = 12.sp,
                fontFamily = ShareTechMono,
                letterSpacing = 1.sp
            )
            Text(
                text = "$value/100",
                color = valueColor,
                fontSize = 14.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.neonGlow(valueColor.copy(alpha = 0.5f), 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(Obsidian)
                .border(1.dp, Color(0xFF1A2235))
        ) {
            val progress = (value.toFloat() / 100f).coerceIn(0f, 1f)
            if (progress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF1A2235), SystemBlue)
                            )
                        )
                )
            }
            // Scanline overlay simulation
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.1f))
            )
        }
        
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Obsidian)
                    .border(width = 1.dp, color = SystemBlue.copy(alpha = 0.3f))
                    .padding(16.dp)
            ) {
                Column {
                    Text("30 DAY HISTORY", color = Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("+5% M/M", color = SystemBlue, fontSize = 14.sp, fontFamily = Rajdhani)
                    // Stub for line graph
                    Box(modifier = Modifier.fillMaxWidth().height(40.dp).background(Color(0xFF1A2235)))
                }
            }
        }
    }
}

@Composable
fun EarningGoal(gold: com.heptre.sololeveling.data.Gold) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ClippedCornerShape(20f))
            .background(Obsidian)
            .border(1.dp, ShadowPurple.copy(alpha = 0.5f), ClippedCornerShape(20f))
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "EARNING GOAL",
                color = Slate,
                fontSize = 12.sp,
                fontFamily = ShareTechMono
            )
            Text(
                text = "GOLD: ${gold.current} / ${gold.target}",
                color = ShadowPurple,
                fontSize = 14.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.neonGlow(ShadowPurple, 5.dp)
            )
        }
    }
}

// Extension function to get display name for stat type
private val StatType.displayName: String
    get() = when (this) {
        StatType.STRENGTH -> "STRENGTH"
        StatType.APPTITUDE -> "APTITUDE"
        StatType.INTELLIGENCE -> "INTELLIGENCE"
        StatType.ENDURANCE -> "ENDURANCE"
    }

// Extension function to get value from Player
private fun Player.getStatValue(statType:StatType): Int {
    return when (statType) {
        StatType.STRENGTH -> stats.strength
        StatType.APPTITUDE -> stats.aptitude
        StatType.INTELLIGENCE -> stats.intelligence
        StatType.ENDURANCE -> stats.endurance
    }
}