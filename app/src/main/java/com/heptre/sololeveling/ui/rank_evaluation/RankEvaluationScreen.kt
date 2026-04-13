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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*

@Composable
fun RankEvaluationScreen(viewModel: RankEvaluationViewModel) {
    val currentRank by viewModel.currentRank.collectAsState()
    val nextRank by viewModel.nextRank.collectAsState()
    
    // Stub values for the UI PRD requirement
    val nextRankStr = nextRank?.name ?: "B"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {
        // Decorative background grid
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Simulate grid/scanlines
        )

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

            // Title
            Text("EVALUATION COMPLETE", color = FrostWhite, fontSize = 32.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.neonGlow(SystemBlue.copy(alpha = 0.3f), 5.dp))
            Text("Cycle 04 Finalized", color = Slate, fontSize = 14.sp, fontFamily = ShareTechMono, letterSpacing = 1.sp)

            Spacer(modifier = Modifier.height(48.dp))

            // Rank Transition
            Text("PROMOTION ACHIEVED", color = SystemBlue, fontSize = 14.sp, fontFamily = ShareTechMono, letterSpacing = 2.sp)
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
                Text("→", color = SystemBlue, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(24.dp))
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$nextRankStr-RANK",
                        color = FrostWhite,
                        fontSize = 48.sp,
                        fontFamily = Rajdhani,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.neonGlow(SystemBlue, 15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Consistency Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .hudGlass()
                    .border(1.dp, SystemBlue)
                    .padding(24.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("CONSISTENCY RATE", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                        Text("92%", color = SystemBlue, fontSize = 16.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Obsidian)) {
                        Box(modifier = Modifier.fillMaxWidth(0.92f).fillMaxHeight().background(SystemBlue))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Quests: 61/63", color = FrostWhite, fontSize = 14.sp, fontFamily = ShareTechMono)
                        Text("Penalties: 0", color = FrostWhite, fontSize = 14.sp, fontFamily = ShareTechMono)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Attribute Adjustments
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AttributeBox(title = "STR", value = "+15")
                AttributeBox(title = "APT", value = "+10")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AttributeBox(title = "INT", value = "+5")
                AttributeBox(title = "END", value = "+8")
            }

            Spacer(modifier = Modifier.height(48.dp))

            // CTA
            Button(
                onClick = { /* Claim New Rank logic and navigate */ },
                shape = ClippedCornerShape(20f),
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = SystemBlue),
                border = androidx.compose.foundation.BorderStroke(1.dp, SystemBlue)
            ) {
                Text("CLAIM NEW RANK", fontSize = 18.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, modifier = Modifier.neonGlow(SystemBlue, 5.dp))
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