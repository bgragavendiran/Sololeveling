package com.heptre.sololeveling.ui.system_penalty

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*

@Composable
fun SystemPenaltyOverlay(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    // Intense Red Background Glow
    val redGlow by animateFloatAsState(
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "redGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f))
            .background(PenaltyRed.copy(alpha = redGlow))
            .glitchBackground()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Alert Badge
            Box(
                modifier = Modifier
                    .background(VoidBlack)
                    .border(width = 1.dp, color = PenaltyRed)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text("SYSTEM ALERT", color = PenaltyRed, fontFamily = ShareTechMono, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "PENALTY OVERRIDE",
                color = FrostWhite,
                fontSize = 48.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.neonGlow(PenaltyRed, 10.dp)
            )

            Text(
                text = "STATUS: ACTIVE",
                color = PenaltyRed,
                fontSize = 14.sp,
                fontFamily = ShareTechMono,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Timer Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Obsidian)
                    .border(1.dp, Color(0xFF1A2235))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("REMAINING_TIME", color = Slate, fontFamily = ShareTechMono, fontSize = 10.sp)
                    Text("PENALTY TIMER", color = PenaltyRed, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("23:59", color = FrostWhite, fontFamily = ShareTechMono, fontSize = 72.sp, modifier = Modifier.neonGlow(PenaltyRed, 15.dp))
                    Text("Failure to complete tasks will result in demise.", color = PenaltyRed, fontFamily = Outfit, fontSize = 12.sp, modifier = Modifier.padding(top = 16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Required Tasks Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .hudGlass()
                    .drawBehind {
                        drawRect(
                            color = PenaltyRed,
                            topLeft = androidx.compose.ui.geometry.Offset(0f, 0f),
                            size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)
                        )
                    }
                    .border(1.dp, Color(0xFF1A2235))
                    .padding(16.dp)
            ) {
                Text("REQUIRED TASKS:", color = FrostWhite, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Workout (30m)", color = PenaltyRed, fontFamily = ShareTechMono, fontSize = 14.sp)
                Text("2. Meditation (15m)", color = PenaltyRed, fontFamily = ShareTechMono, fontSize = 14.sp)
                Text("3. Study (45m)", color = PenaltyRed, fontFamily = ShareTechMono, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Acceptance Button
            Button(
                onClick = onAccept,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(ClippedCornerShape(20f))
                    .neonGlow(PenaltyRed, 20.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PenaltyRed,
                    contentColor = VoidBlack
                )
            ) {
                Text(
                    text = "ACCEPT PUNISHMENT",
                    fontSize = 18.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}