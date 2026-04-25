package com.heptre.sololeveling.ui.quest_log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.heptre.sololeveling.ui.theme.*

@Composable
fun GymLogDialog(
    currentSessions: Int,
    targetSessions: Int,
    onDismiss: () -> Unit,
    onLog: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ClippedCornerShape(20f))
                .hudGlass()
                .border(2.dp, SystemBlue.copy(alpha = 0.7f), ClippedCornerShape(20f))
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "LOG GYM SESSION",
                    color = FrostWhite,
                    fontSize = 20.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$currentSessions / $targetSessions SESSIONS THIS WEEK",
                    color = SystemBlue,
                    fontSize = 12.sp,
                    fontFamily = ShareTechMono,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Obsidian)
                        .border(1.dp, Color(0xFF1A2235))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            "CONFIRM 1-HOUR GYM SESSION",
                            color = FrostWhite,
                            fontSize = 14.sp,
                            fontFamily = Rajdhani,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Logging a session confirms you completed at least 1 hour of gym training. The System does not tolerate deception, Hunter.",
                            color = Slate,
                            fontSize = 11.sp,
                            fontFamily = Outfit,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCEL", color = Slate, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = onLog,
                        colors = ButtonDefaults.buttonColors(containerColor = SystemBlue, contentColor = VoidBlack),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("CONFIRM", fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
