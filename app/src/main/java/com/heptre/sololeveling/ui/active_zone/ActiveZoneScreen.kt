package com.heptre.sololeveling.ui.active_zone

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*
import kotlinx.coroutines.delay

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
    
    LaunchedEffect(shouldAbort) {
        if (shouldAbort) {
            abortProgress = 0
            while (abortProgress < 100 && isActive) {
                delay(30)
                abortProgress++
                if (abortProgress >= 100) {
                    viewModel.abortQuest()
                }
            }
        } else {
            abortProgress = 0
        }
    }

    // Timer formatting
    val minutes = timerValue / 60
    val seconds = timerValue % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    val maxTime = 45 * 60f // Replace with actual total duration
    val progressRatio = if (maxTime > 0) timerValue.toFloat() / maxTime else 0f

    // Pulsing Text Animation
    val pulse by animateFloatAsState(
        targetValue = if (isRunning) 1f else 0.5f,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("SYSTEM ENGAGEMENT", color = SystemBlue, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.neonGlow(SystemBlue, 5.dp))
            Text("Aptitude Quest: Deep Work", color = Slate, fontSize = 14.sp, fontFamily = ShareTechMono)
        }

        // Circular Timer Visuals
        Box(
            modifier = Modifier.align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {
            // Concentric Decorative Rings
            Canvas(modifier = Modifier.size(340.dp)) {
                drawCircle(color = SystemBlue.copy(alpha = 0.1f), style = Stroke(width = 1.dp.toPx()))
            }
            Canvas(modifier = Modifier.size(360.dp)) {
                drawCircle(
                    color = SystemBlue.copy(alpha = 0.2f),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                )
            }
            Canvas(modifier = Modifier.size(300.dp)) {
                drawCircle(color = SystemBlue.copy(alpha = 0.05f), style = Stroke(width = 4.dp.toPx()))
            }
            
            // Progress Ring
            Canvas(modifier = Modifier.size(280.dp)) {
                // Background Track
                drawArc(
                    color = Obsidian,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
                // Active System Blue Track
                drawArc(
                    color = SystemBlue,
                    startAngle = -90f,
                    sweepAngle = 360f * progressRatio,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Crosshair Markers
            Canvas(modifier = Modifier.size(380.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val lineLength = 20.dp.toPx()
                drawLine(color = SystemBlue.copy(alpha = 0.5f), start = Offset(center.x, 0f), end = Offset(center.x, lineLength), strokeWidth = 2f)
                drawLine(color = SystemBlue.copy(alpha = 0.5f), start = Offset(center.x, size.height - lineLength), end = Offset(center.x, size.height), strokeWidth = 2f)
                drawLine(color = SystemBlue.copy(alpha = 0.5f), start = Offset(0f, center.y), end = Offset(lineLength, center.y), strokeWidth = 2f)
                drawLine(color = SystemBlue.copy(alpha = 0.5f), start = Offset(size.width - lineLength, center.y), end = Offset(size.width, center.y), strokeWidth = 2f)
            }

            // Central Timer Text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formattedTime,
                    color = SystemBlue,
                    fontSize = 80.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.neonGlow(SystemBlue, 15.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SYNC ACTIVE",
                    color = SystemBlue,
                    fontSize = 12.sp,
                    fontFamily = ShareTechMono,
                    modifier = Modifier.alpha(pulse)
                )
            }
        }

        // Abort Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "(Hold 3s to override)",
                    color = Slate,
                    fontSize = 10.sp,
                    fontFamily = ShareTechMono,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(64.dp)
                        .clip(ClippedCornerShape(20f))
                        .background(VoidBlack)
                        .border(1.dp, PenaltyRed.copy(alpha = 0.4f), ClippedCornerShape(20f))
                        .clickable { /* Handle press and hold elsewhere if possible, or tap triggers state */ viewModel.abortQuest() }
                ) {
                    // Fill background for abort progress
                    if (abortProgress > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(abortProgress / 100f)
                                .background(PenaltyRed.copy(alpha = 0.3f))
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ABORT QUEST",
                            color = PenaltyRed,
                            fontSize = 18.sp,
                            fontFamily = Rajdhani,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            modifier = Modifier.neonGlow(PenaltyRed.copy(alpha=0.5f), 5.dp)
                        )
                    }
                }
            }
        }
    }
}
