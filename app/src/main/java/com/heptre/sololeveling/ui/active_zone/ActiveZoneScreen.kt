package com.heptre.sololeveling.ui.active_zone

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.heptre.sololeveling.ui.theme.*

@Composable
fun ActiveZoneScreen(viewModel: ActiveZoneViewModel) {
    val isActive by viewModel.isActive.collectAsState()
    val timerValue by viewModel.timerValue.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val hasFailed by viewModel.hasFailed.collectAsState()
    val hasCompleted by viewModel.hasCompleted.collectAsState()
    val shouldAbort by viewModel.shouldAbort.collectAsState()

    // Abort button logic
    var abortProgress by remember { mutableIntStateOf(0) }
    val abortAnimation by animateIntAsState(
        targetValue = abortProgress,
        animationSpec = tween(
            durationMillis = 3000,
            easing = LinearEasing
        ),
        label = "abortProgress"
    )
    
    LaunchedEffect(shouldAbort) {
        if (shouldAbort) {
            abortProgress = 0
            while (abortProgress < 100 && isActive) {
                delay(30)
                abortProgress++
            }
        }
    }

    // Timer formatting
    val minutes = timerValue / 60
    val seconds = timerValue % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    // Pulsing ring animation
    val pulse by animateFloatAsState(
        targetValue = if (isRunning) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {
        // Pulsing Ring
        if (isRunning) {
            Box(
                modifier = Modifier
                    .size((minutes + seconds).times(20.dp) + 200.dp)
                    .alpha(pulse * 0.3f)
                    .clip(CircleShape)
                    .background(SystemBlue)
            )
        }

        // Timer
        Text(
            text = formattedTime,
            color = SystemBlue,
            fontSize = 120.sp,
            fontFamily = Rajdhani,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .shadow(10.dp, SystemBlue, CircleShape)
        )

        // Abort Button
        AbortButton(
            isActive = isActive,
            isRunning = isRunning,
            shouldAbort = shouldAbort,
            abortProgress = abortProgress,
            onAbort = { viewModel.abortQuest() }
        )

        // Completion screen
        if (hasCompleted) {
            CompletedScreen()
        }

        // Failure screen
        if (hasFailed) {
            FailureScreen(
                onAccept = { viewModel.acceptPenalty() },
                onDismiss = { viewModel.dismissPenalty() }
            )
        }
    }
}

@Composable
fun AbortButton(
    isActive: Boolean,
    isRunning: Boolean,
    shouldAbort: Boolean,
    abortProgress: Int,
    onAbort: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 32.dp)
            .padding(horizontal = 32.dp)
    ) {
        Button(
            onClick = onAbort,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = PenaltyRed
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                color = PenaltyRed,
                width = 2.dp
            )
        ) {
            Text(
                text = "ABORT QUEST (INCURS PENALTY)",
                color = PenaltyRed,
                fontSize = 16.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun CompletedScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E).copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "QUEST COMPLETE",
                color = SystemBlue,
                fontSize = 32.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "+5 APTITUDE",
                color = SystemBlue,
                fontSize = 24.sp,
                fontFamily = ShareTechMono,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Return to Quest Log",
                color = FrostWhite,
                fontSize = 14.sp,
                fontFamily = Outfit
            )
        }
    }
}

@Composable
fun FailureScreen(
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A0000).copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Flashing warning header
            Text(
                text = "SYSTEM ALERT",
                color = PenaltyRed,
                fontSize = 32.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.animateContentSize()
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "YOU HAVE FAILED TO COMPLETE 3 CONSECUTIVE QUESTS.",
                color = FrostWhite,
                fontSize = 16.sp,
                fontFamily = Outfit,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "-1 RANK. -10 STRENGTH.",
                color = PenaltyRed,
                fontSize = 20.sp,
                fontFamily = Rajdhani,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = onAccept,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PenaltyRed,
                    contentColor = Color.Black
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