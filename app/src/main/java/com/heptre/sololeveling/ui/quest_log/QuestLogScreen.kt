















































































































































package com.heptre.sololeveling.ui.quest_log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.heptre.sololeveling.data.Quest

@Composable
fun QuestLogScreen(viewModel: QuestLogViewModel) {
    val dailyQuests by viewModel.dailyQuests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VoidBlack)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "DAILY QUESTS",
                    color = SystemBlue,
                    fontSize = 14.sp,
                    fontFamily = ShareTechMono,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VoidBlack)
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Text(
                        text = "LOADING QUESTS...",
                        color = Slate,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        fontSize = 14.sp,
                        fontFamily = ShareTechMono
                    )
                }
                dailyQuests.isEmpty() -> {
                    Text(
                        text = "ALL QUESTS COMPLETE. STANDBY.",
                        color = FrostWhite,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        fontSize = 16.sp,
                        fontFamily = Outfit
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Category: Strength
                        item {
                            CategoryHeader("STRENGTH (FITNESS)")
                        }

                        items(dailyQuests.filter { it.statType == com.heptre.sololeveling.data.Quest.StatType.STRENGTH }) { quest ->
                            QuestRow(
                                quest = quest,
                                onToggle = { viewModel.toggleQuestCompletion(quest) },
                                onSkip = { viewModel.skipQuest(quest) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Category: Intelligence
                        item {
                            CategoryHeader("INTELLIGENCE (MIND)")
                        }

                        items(dailyQuests.filter { it.statType == com.heptre.sololeveling.data.Quest.StatType.INTELLIGENCE }) { quest ->
                            QuestRow(
                                quest = quest,
                                onToggle = { viewModel.toggleQuestCompletion(quest) },
                                onSkip = { viewModel.skipQuest(quest) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Category: Aptitude
                        item {
                            CategoryHeader("APTITUDE (DEEP WORK)")
                        }

                        items(dailyQuests.filter { it.statType == com.heptre.sololeveling.data.Quest.StatType.APPTITUDE }) { quest ->
                            QuestRow(
                                quest = quest,
                                onToggle = { viewModel.toggleQuestCompletion(quest) },
                                onSkip = { viewModel.skipQuest(quest) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Category: Endurance
                        item {
                            CategoryHeader("ENDURANCE (SLEEP)")
                        }

                        items(dailyQuests.filter { it.statType == com.heptre.sololeveling.data.Quest.StatType.ENDURANCE }) { quest ->
                            QuestRow(
                                quest = quest,
                                onToggle = { viewModel.toggleQuestCompletion(quest) },
                                onSkip = { viewModel.skipQuest(quest) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(categoryName: String) {
    Text(
        text = categoryName,
        color = SystemBlue,
        fontSize = 14.sp,
        fontFamily = ShareTechMono,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = Obsidian.copy(alpha = 0.4f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
fun QuestRow(
    quest: Quest,
    onToggle: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Obsidian.copy(alpha = 0.8f))
            .border(1.dp, Slate.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (quest.isCompleted) SystemBlue else Obsidian.copy(alpha = 0.5f))
                .border(
                    width = 1.dp,
                    color = SystemBlue,
                    shape = RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (quest.isCompleted) {
                Icons.Default.Check(
                    contentDescription = "Completed",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Quest Title
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = quest.title,
                color = if (quest.isCompleted) Slate else FrostWhite,
                fontSize = 16.sp,
                fontFamily = Outfit,
                textDecoration = if (quest.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            if (quest.description.isNotEmpty()) {
                Text(
                    text = quest.description,
                    color = Slate,
                    fontSize = 12.sp,
                    fontFamily = ShareTechMono
                )
            }
        }

        // Reward
        Text(
            text = "+${quest.reward} ${quest.statType.displayName}",
            color = when {
                quest.isCompleted -> SystemBlue
                quest.isSkipped -> PenaltyRed
                else -> FrostWhite
            },
            fontSize = 14.sp,
            fontFamily = ShareTechMono,
            fontWeight = FontWeight.Bold
        )
    }
}