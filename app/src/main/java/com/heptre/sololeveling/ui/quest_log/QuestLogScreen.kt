package com.heptre.sololeveling.ui.quest_log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.db.QuestEntity
import com.heptre.sololeveling.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestLogScreen(viewModel: QuestLogViewModel) {
    val dailyQuests by viewModel.dailyQuests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
    val syncRate by viewModel.syncRate.collectAsState()
    val level by viewModel.level.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val rankLabel = playerState?.rank?.name?.let { "RANK $it" } ?: "RANK ?"
    val syncStatus = when {
        syncRate >= 80 -> "Recovery Optimal"
        syncRate >= 50 -> "Sync Degraded"
        else -> "Low Sync"
    }
    val syncColor = when {
        syncRate >= 80 -> TertiaryRecovery
        syncRate >= 50 -> SystemBlue
        else -> PenaltyRed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {
        // Sticky Header Group: Rank Card & Mind Sync
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .hudGlass()
                .border(width = 1.dp, color = SystemBlue.copy(alpha = 0.3f))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(rankLabel, color = SystemBlue, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.neonGlow(SystemBlue.copy(alpha=0.5f), 5.dp))
                    Text("Level $level Hunter", color = FrostWhite, fontSize = 12.sp, fontFamily = ShareTechMono)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("SYNC: $syncRate%", color = syncColor, fontSize = 16.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    Text(syncStatus, color = Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "ACTIVE QUESTS",
            color = FrostWhite,
            fontSize = 18.sp,
            fontFamily = Rajdhani,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "LOADING QUESTS...",
                    color = Slate,
                    fontSize = 14.sp,
                    fontFamily = ShareTechMono
                )
            }
        } else if (dailyQuests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "ALL QUESTS COMPLETE. STANDBY.",
                    color = FrostWhite,
                    fontSize = 16.sp,
                    fontFamily = Outfit
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Group by StatType for visual organization if desired or just list them
                // The PRD mentions left borders per category. We will just render the list flatly 
                // but color the borders based on type.
                
                items(
                    items = dailyQuests,
                    key = { it.id }
                ) { quest ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.StartToEnd || it == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.skipQuest(quest)
                                true
                            } else {
                                false
                            }
                        }
                    )
                    
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(PenaltyRed.copy(alpha = 0.2f))
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text("SKIP (100 GOLD)", color = PenaltyRed, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        QuestRow(
                            quest = quest,
                            onToggle = { viewModel.toggleQuestCompletion(quest) }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(ClippedCornerShape(20f))
                            .border(1.dp, SystemBlue.copy(alpha=0.5f), ClippedCornerShape(20f)),
                        colors = ButtonDefaults.buttonColors(containerColor = VoidBlack, contentColor = SystemBlue)
                    ) {
                        Text("+ INITIALIZE CUSTOM QUEST", fontFamily = ShareTechMono, letterSpacing = 2.sp)
                    }
                    Spacer(modifier = Modifier.height(80.dp)) // Nav pad
                }
            }
        }
    }

    if (showAddDialog) {
        AddQuestDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, desc, stat, reward ->
                viewModel.addCustomQuest(title, desc, stat, reward)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun QuestRow(
    quest: QuestEntity,
    onToggle: () -> Unit
) {
    val borderColor = when (quest.statType) {
        StatType.STRENGTH -> SystemBlue.copy(alpha = 0.6f)
        StatType.INTELLIGENCE -> FrostWhite.copy(alpha = 0.2f)
        StatType.ENDURANCE -> SystemBlue.copy(alpha = 0.6f)
        StatType.APPTITUDE -> TertiaryRecovery.copy(alpha = 0.4f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .hudGlass()
            // The left 4px colored border
            .drawBehind {
                drawRect(
                    color = borderColor,
                    topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                    size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)
                )
            }
            .border(1.dp, Color(0xFF1A2235))
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(if (quest.isCompleted) SystemBlue else Obsidian)
                .sharpBorder(SystemBlue, 1.dp),
            contentAlignment = Alignment.Center
        ) {
            if (quest.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = VoidBlack,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Quest Details
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = quest.title.uppercase(),
                color = if (quest.isCompleted) Slate else FrostWhite,
                fontSize = 18.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                textDecoration = if (quest.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${quest.statType.name} CATEGORY",
                    color = Slate,
                    fontSize = 10.sp,
                    fontFamily = ShareTechMono,
                    letterSpacing = 1.sp
                )
            }
        }

        // Reward Value
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "+${quest.reward}",
                color = if (quest.isCompleted) SystemBlue else FrostWhite,
                fontSize = 16.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = quest.statType.name.take(3),
                color = Slate,
                fontSize = 10.sp,
                fontFamily = ShareTechMono
            )
        }
    }
}