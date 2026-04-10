package com.heptre.sololeveling.ui.status_window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.heptre.sololeveling.data.Player
import com.heptre.sololeveling.data.Rank
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
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    fontSize = 18.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // Handle empty state
        player.stats.strength == 0 && player.stats.aptitude == 0 && player.stats.intelligence == 0 && player.stats.endurance == 0 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VoidBlack)
            ) {
                Text(
                    text = "NO STATS RECORDED. BEGIN QUESTS.",
                    color = FrostWhite,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    fontSize = 16.sp,
                    fontFamily = Outfit
                )
            }
        }
        else -> {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VoidBlack)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = player.name,
                        color = FrostWhite,
                        fontSize = 20.sp,
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = player.title,
                        color = SystemBlue,
                        fontSize = 14.sp,
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rank Badge
                RankBadge(rank = player.rank)

                Spacer(modifier = Modifier.height(24.dp))

                // Stat Bars
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "CORE STATS",
                            color = Slate,
                            fontSize = 12.sp,
                            fontFamily = ShareTechMono,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    
                    item {
                        for (statType in listOf(StatType.STRENGTH, StatType.APPTITUDE, StatType.INTELLIGENCE, StatType.ENDURANCE)) {
                            StatBar(
                                statType = statType,
                                value = player.getStatValue(statType),
                                isExpanded = isExpanded,
                                onStatClick = { viewModel.toggleStatExpansion() }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                // Earning Goal
                EarningGoal(gold = player.gold)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun RankBadge(rank: Rank) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(4.dp)) // Slight rounding for better readability
            .background(Obsidian.copy(alpha = 0.8f))
            .border(2.dp, SystemBlue, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rank.displayText,
            color = when (rank) {
                Rank.S -> SystemBlue
                else -> FrostWhite
            },
            fontSize = 28.sp,
            fontFamily = Rajdhani,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
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
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Obsidian.copy(alpha = 0.6f))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = statName,
                color = if (isAnyZero) Slate else SystemBlue,
                fontSize = 12.sp,
                fontFamily = ShareTechMono
            )
            Text(
                text = "$value",
                color = if (isAnyZero) Slate else SystemBlue,
                fontSize = 12.sp,
                fontFamily = ShareTechMono
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { value.toFloat() / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            color = SystemBlue,
            trackColor = Slate.copy(alpha = 0.3f),
            gapSize = 0.dp,
            drawBehindBackground = true
        )
    }
}

@Composable
fun EarningGoal(gold: com.heptre.sololeveling.data.Gold) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Obsidian.copy(alpha = 0.6f))
            .padding(16.dp)
    ) {
        Text(
            text = "GOLD: ${gold.current} / ${gold.target}",
            color = ShadowPurple,
            fontSize = 16.sp,
            fontFamily = Rajdhani,
            fontWeight = FontWeight.Bold
        )
    }
}

// Extension function to get display name for stat type
private fun StatType.displayName: String {
    return when (this) {
        StatType.STRENGTH -> "STRENGTH"
        StatType.APPTITUDE -> "APTITUDE"
        StatType.INTELLIGENCE -> "INTELLIGENCE"
        StatType.ENDURANCE -> "ENDURANCE"
    }
}

// Extension function to get value from Player
private fun Player.getStatValue(statType: com.heptre.sololeveling.data.Stats.StatType): Int {
    return when (statType) {
        com.heptre.sololeveling.data.Stats.StatType.STRENGTH -> stats.strength
        com.heptre.sololeveling.data.Stats.StatType.APPTITUDE -> stats.aptitude
        com.heptre.sololeveling.data.Stats.StatType.INTELLIGENCE -> stats.intelligence
        com.heptre.sololeveling.data.Stats.StatType.ENDURANCE -> stats.endurance
    }
}

// Extension function to get display text for Rank
private val Rank.displayText: String
    get() {
        return when (this) {
            Rank.UNRANKED -> "PLAYER"
            Rank.S -> "SHADOW MONARCH"
            else -> name
        }
    }