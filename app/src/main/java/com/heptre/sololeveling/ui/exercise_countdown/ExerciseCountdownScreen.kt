package com.heptre.sololeveling.ui.exercise_countdown

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.heptre.sololeveling.data.tts.TtsManager
import com.heptre.sololeveling.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExerciseCountdownScreen(
    viewModel: ExerciseCountdownViewModel,
    onComplete: () -> Unit,
    onFailed: () -> Unit
) {
    val phase            by viewModel.phase.collectAsState()
    val currentStepIndex by viewModel.currentStepIndex.collectAsState()
    val currentRep       by viewModel.currentRep.collectAsState()
    val hasFailed        by viewModel.hasFailed.collectAsState()
    val hasCompleted     by viewModel.hasCompleted.collectAsState()

    val context      = LocalContext.current
    val ttsManager   = remember { TtsManager(context) }
    val coroutine    = rememberCoroutineScope()
    val lifecycle    = LocalLifecycleOwner.current.lifecycle

    // Pipe TTS events from ViewModel → TtsManager
    LaunchedEffect(Unit) {
        viewModel.ttsEvent.collect { ttsManager.speak(it) }
    }

    // Detect when user leaves screen (ON_PAUSE = failure)
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) viewModel.onUserLeft()
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            ttsManager.shutdown()
        }
    }

    LaunchedEffect(hasCompleted) { if (hasCompleted) onComplete() }
    LaunchedEffect(hasFailed) {
        if (hasFailed) {
            delay(3000)
            onFailed()
        }
    }

    val currentExercise = viewModel.exercises[currentStepIndex]
    val accentColor     = if (hasFailed) PenaltyRed else SystemBlue

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Top header ──────────────────────────────────────
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(56.dp))

                Box(
                    modifier = Modifier
                        .border(1.dp, accentColor)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        "EXERCISE PROTOCOL ${currentStepIndex + 1} / ${viewModel.totalSteps}",
                        color = accentColor,
                        fontSize = 11.sp,
                        fontFamily = ShareTechMono
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = when (phase) {
                        ExercisePhase.REST     -> "REST — PREPARING"
                        ExercisePhase.COMPLETE -> "ALL PROTOCOLS COMPLETE"
                        ExercisePhase.FAILED   -> "PROTOCOL FAILED"
                        else                   -> currentExercise.name
                    },
                    color = FrostWhite,
                    fontSize = 34.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.neonGlow(accentColor.copy(alpha = 0.4f), 8.dp)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${currentExercise.stat.name} · ${currentExercise.reps} REPS",
                    color = Slate,
                    fontSize = 11.sp,
                    fontFamily = ShareTechMono,
                    letterSpacing = 1.sp
                )
            }

            // ── Central rep counter ─────────────────────────────
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(220.dp)) {
                    drawCircle(
                        color = accentColor.copy(alpha = 0.08f)
                    )
                    drawCircle(
                        color = accentColor.copy(alpha = 0.5f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    drawCircle(
                        color = accentColor.copy(alpha = 0.15f),
                        radius = size.minDimension / 2.2f,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                Text(
                    text = when (phase) {
                        ExercisePhase.INTRO    -> "READY"
                        ExercisePhase.REST     -> "REST"
                        ExercisePhase.COMPLETE -> "DONE"
                        ExercisePhase.FAILED   -> "FAIL"
                        else                   -> if (currentRep == 0) "✓" else "$currentRep"
                    },
                    color = accentColor,
                    fontSize = if (phase == ExercisePhase.INTRO || phase == ExercisePhase.REST) 48.sp else 96.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.neonGlow(accentColor, 20.dp)
                )
            }

            // ── Exercise progress dots ──────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                viewModel.exercises.forEachIndexed { idx, _ ->
                    val isDone    = idx < currentStepIndex || phase == ExercisePhase.COMPLETE
                    val isCurrent = idx == currentStepIndex
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 12.dp else 8.dp)
                            .background(
                                when {
                                    isDone    -> TertiaryRecovery
                                    isCurrent -> accentColor
                                    else      -> Slate.copy(alpha = 0.25f)
                                }
                            )
                    )
                }
            }

            // ── Bottom action ───────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (phase) {
                    ExercisePhase.INTRO -> {
                        Button(
                            onClick = { viewModel.start() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(ClippedCornerShape(20f))
                                .border(1.dp, SystemBlue, ClippedCornerShape(20f)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = SystemBlue
                            )
                        ) {
                            Text(
                                "BEGIN PROTOCOL",
                                fontSize = 20.sp,
                                fontFamily = Rajdhani,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.neonGlow(SystemBlue, 5.dp)
                            )
                        }
                    }

                    ExercisePhase.ACTIVE, ExercisePhase.REST -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, PenaltyRed.copy(alpha = 0.4f))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "WARNING: Leaving this screen marks the quest as FAILED",
                                color = PenaltyRed.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontFamily = ShareTechMono,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    ExercisePhase.FAILED -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("PROTOCOL FAILED", color = PenaltyRed, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("Penalty applied. Returning to base...", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                        }
                    }

                    ExercisePhase.COMPLETE -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("PROTOCOL COMPLETE", color = TertiaryRecovery, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("STR + END increased. Well done, Hunter.", color = Slate, fontSize = 12.sp, fontFamily = ShareTechMono)
                        }
                    }
                }

                Spacer(Modifier.height(48.dp))
            }
        }
    }
}
