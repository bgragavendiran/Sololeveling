package com.heptre.sololeveling.ui.rank_evaluation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.ui.theme.*

@Composable
fun RankEvaluationScreen(viewModel: RankEvaluationViewModel) {
    val currentRank by viewModel.currentRank.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val nextRank by viewModel.nextRank.collectAsState()

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
                    text = "RANK EVALUATION",
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Current Rank Display
                Text(
                    text = "CURRENT RANK",
                    color = Slate,
                    fontSize = 14.sp,
                    fontFamily = ShareTechMono,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                RankBadge(rank = currentRank)

                Spacer(modifier = Modifier.height(48.dp))

                // Progress Bar
                Text(
                    text = "PROGRESS TO NEXT RANK",
                    color = Slate,
                    fontSize = 14.sp,
                    fontFamily = ShareTechMono,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ProgressSection(progress = progress)

                Spacer(modifier = Modifier.height(48.dp))

                // Next Rank Info
                nextRank?.let { next ->
                    Text(
                        text = "NEXT RANK: ${next.name}",
                        color = when (next) {
                            Rank.S -> SystemBlue
                            else -> FrostWhite
                        },
                        fontSize = 20.sp,
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "Requirements: Complete 21-day cycle",
                        color = Slate,
                        fontSize = 14.sp,
                        fontFamily = Outfit,
                        textAlign = TextAlign.Center
                    )
                } ?: run {
                    Text(
                        text = "CONGRATULATIONS!",
                        color = ShadowPurple,
                        fontSize = 32.sp,
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "You have reached the pinnacle of the system.",
                        color = FrostWhite,
                        fontSize = 16.sp,
                        fontFamily = Outfit,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "SHADOW MONARCH TITLE UNLOCKED",
                        color = ShadowPurple,
                        fontSize = 24.sp,
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 21-Day Cycle Info
                Text(
                    text = "21-DAY CYCLE",
                    color = Slate,
                    fontSize = 14.sp,
                    fontFamily = ShareTechMono,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CycleInfo()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun RankBadge(rank: Rank) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Obsidian.copy(alpha = 0.8f))
            .border(2.dp, when (rank) {
                Rank.S -> SystemBlue
                else -> SystemBlue
            }, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rank.name,
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
fun ProgressSection(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Obsidian.copy(alpha = 0.6f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    color = if (progress >= 1f) ShadowPurple else SystemBlue
                )
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            color = FrostWhite,
            fontSize = 12.sp,
            fontFamily = ShareTechMono,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun CycleInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Obsidian.copy(alpha = 0.6f))
            .padding(16.dp)
    ) {
        Text(
            text = "CYCLE TRACKING",
            color = SystemBlue,
            fontSize = 14.sp,
            fontFamily = ShareTechMono,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "21-day cycles determine rank progression.\n\n" +
                   "• Complete daily quests\n" +
                   "• Maintain streaks\n" +
                   "• Avoid system penalties\n" +
                   "• Contribute to your growth",
            color = FrostWhite,
            fontSize = 14.sp,
            fontFamily = Outfit,
            lineHeight = 24.sp
        )
    }
}