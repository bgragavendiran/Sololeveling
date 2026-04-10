package com.heptre.sololeveling.ui.system_penalty

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.heptre.sololeveling.ui.theme.*

@Composable
fun SystemPenaltyOverlay(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    // Flashing animation for the screen
    val alpha by animateFloatAsState(
        targetValue = if (isPenaltyActive) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "penaltyFlash"
    )

    // Red tint overlay
    val redTint by animateFloatAsState(
        targetValue = if (isPenaltyActive) 0.9f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "redTint"
    )

    LaunchedEffect(Unit) {
        isPenaltyActive = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000).copy(alpha = redTint))
    ) {
        // Penalty Modal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Warning Header
            Text(
                text = "SYSTEM ALERT",
                color = PenaltyRed,
                fontSize = 32.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .animateContentSize()
            )

            // Failure Reason
            Text(
                text = "YOU HAVE FAILED TO COMPLETE 3 CONSECUTIVE QUESTS.",
                color = FrostWhite,
                fontSize = 18.sp,
                fontFamily = Outfit,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Penalty Result
            Text(
                text = "-1 RANK. -10 STRENGTH.",
                color = PenaltyRed,
                fontSize = 24.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Acceptance Button
            Button(
                onClick = onAccept,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PenaltyRed,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Text(
                    text = "ACCEPT PUNISHMENT",
                    fontSize = 16.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

var isPenaltyActive by mutableStateOf(false)