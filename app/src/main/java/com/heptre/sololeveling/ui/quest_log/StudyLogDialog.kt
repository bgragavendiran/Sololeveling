package com.heptre.sololeveling.ui.quest_log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.heptre.sololeveling.ui.theme.*

@Composable
fun StudyLogDialog(
    currentMinutes: Int,
    targetMinutes: Int,
    onDismiss: () -> Unit,
    onLog: (subject: String, minutes: Int) -> Unit
) {
    var selectedSubject by remember { mutableStateOf("DUOLINGO") }
    var minutesText by remember { mutableStateOf("30") }
    val subjects = listOf("DUOLINGO", "BOOKS", "COURSES", "OTHER")

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
                    "LOG STUDY SESSION",
                    color = FrostWhite,
                    fontSize = 20.sp,
                    fontFamily = Rajdhani,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "$currentMinutes / $targetMinutes MINUTES",
                    color = SystemBlue,
                    fontSize = 12.sp,
                    fontFamily = ShareTechMono,
                    letterSpacing = 1.sp
                )

                Spacer(Modifier.height(20.dp))

                Text("STUDY SUBJECT", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (subject in subjects) {
                        val isSelected = selectedSubject == subject
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(if (isSelected) SystemBlue.copy(alpha = 0.2f) else Obsidian)
                                .border(1.dp, if (isSelected) SystemBlue else Color(0xFF1A2235))
                                .clickable { selectedSubject = subject },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                subject.take(3),
                                color = if (isSelected) SystemBlue else Slate,
                                fontSize = 10.sp,
                                fontFamily = ShareTechMono,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text("DURATION (MINUTES)", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(Modifier.height(4.dp))
                BasicTextField(
                    value = minutesText,
                    onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() }) minutesText = it },
                    textStyle = TextStyle(color = FrostWhite, fontSize = 16.sp, fontFamily = Outfit),
                    cursorBrush = SolidColor(SystemBlue),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(VoidBlack)
                                .border(1.dp, Color(0xFF1A2235))
                                .padding(12.dp)
                        ) {
                            if (minutesText.isEmpty()) {
                                Text("30", color = Slate.copy(alpha = 0.5f), fontSize = 16.sp, fontFamily = Outfit)
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCEL", color = Slate, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = {
                            val mins = minutesText.toIntOrNull() ?: return@Button
                            if (mins > 0) onLog(selectedSubject, mins)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SystemBlue, contentColor = VoidBlack),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("LOG", fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
