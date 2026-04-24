package com.heptre.sololeveling.ui.rank_evaluation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*

@Composable
fun RankEvaluationScreen(
    viewModel: RankEvaluationViewModel,
    onRankClaimed: () -> Unit = {}
) {
    val currentRank by viewModel.currentRank.collectAsState()
    val nextRank by viewModel.nextRank.collectAsState()
    val cycleStats by viewModel.cycleStats.collectAsState()
    val cycleNumber by viewModel.cycleNumber.collectAsState()
    val rankClaimed by viewModel.rankClaimed.collectAsState()

    LaunchedEffect(rankClaimed) {
        if (rankClaimed) onRankClaimed()
    }

    val nextRankLabel = nextRank?.name ?: currentRank.name
    val isPromotion = cycleStats.consistencyPercent >= 66
    val resultLabel = if (isPromotion) "PROMOTION ACHIEVED" else "DEMOTION INCURRED"
    val resultColor = if (isPromotion) SystemBlue else PenaltyRed

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // System Message Badge
            Box(
                modifier = Modifier
                    .border(1.dp, SystemBlue)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text("SYSTEM MESSAGE", color = SystemBlue, fontSize = 12.sp, fontFamily = ShareTechMono)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "EVALUATION COMPLETE",
                color = FrostWhite,
                fontSize = 32.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.neonGlow(SystemBlue.copy(alpha = 0.3f), 5.dp)
            )
            Text(
                "Cycle $cycleNumber Finalized",
                color = Slate,
                fontSize = 14.sp,
                fontFamily = ShareTechMono,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(resultLabel, color = resultColor, fontSize = 14.sp, fontFamily = ShareTechMono, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${currentRank.name}-RANK",
                    color = Slate,
                    fontSize = 32.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text("→", color = resultColor, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "$nextRankLabel-RANK",
                    color = FrostWhite,
                    fontSize = 48.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.neonGlow(resultColor, 15.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Consistency Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hudGlass()
                    .border(1.dp, resultColor)
                    .padding(24.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("CONSISTENCY RATE", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                        Text(
                            "${cycleStats.consistencyPercent}%",
                            color = resultColor,
                            fontSize = 16.sp,
                            fontFamily = Rajdhani,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Obsidian)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(cycleStats.consistencyPercent / 100f)
                                .fillMaxHeight()
                                .background(resultColor)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Quests: ${cycleStats.completedQuests}/${cycleStats.totalQuests}", color = FrostWhite, fontSize = 14.sp, fontFamily = ShareTechMono)
                        Text("Penalties: ${cycleStats.penalties}", color = FrostWhite, fontSize = 14.sp, fontFamily = ShareTechMono)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Attribute Adjustments
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AttributeBox(title = "STR", value = "+${viewModel.rankBonuses["STR"] ?: 0}")
                AttributeBox(title = "APT", value = "+${viewModel.rankBonuses["APT"] ?: 0}")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AttributeBox(title = "INT", value = "+${viewModel.rankBonuses["INT"] ?: 0}")
                AttributeBox(title = "END", value = "+${viewModel.rankBonuses["END"] ?: 0}")
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { viewModel.claimNewRank() },
                shape = ClippedCornerShape(20f),
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = resultColor),
                border = androidx.compose.foundation.BorderStroke(1.dp, resultColor)
            ) {
                Text(
                    "CLAIM NEW RANK",
                    fontSize = 18.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.neonGlow(resultColor, 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RowScope.AttributeBox(title: String, value: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .background(Obsidian)
            .border(1.dp, Color(0xFF1A2235))
            .padding(16.dp)
    ) {
        Column {
            Text(title, color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = SystemBlue, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
        }
    }
}
