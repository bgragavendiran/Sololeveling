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
import com.heptre.sololeveling.data.QuestFrequency
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.ui.theme.*

@Composable
fun AddQuestDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, description: String, statType: StatType, reward: Int, frequency: QuestFrequency, deadlineHours: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var reward by remember { mutableStateOf("10") }
    var selectedStat by remember { mutableStateOf(StatType.STRENGTH) }
    var selectedFrequency by remember { mutableStateOf(QuestFrequency.DAILY) }
    var deadlineHoursText by remember { mutableStateOf("24") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ClippedCornerShape(20f))
                .hudGlass()
                .border(2.dp, SystemBlue, ClippedCornerShape(20f))
                .padding(24.dp)
        ) {
            Column {
                Text("INITIALIZE CUSTOM QUEST", color = FrostWhite, fontSize = 20.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold, modifier = Modifier.neonGlow(SystemBlue.copy(alpha=0.5f), 5.dp))
                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text("QUEST TITLE", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(value = title, onValueChange = { title = it }, placeholder = "e.g., Run 5km")
                
                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text("OPTIONAL DETAILS", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(value = description, onValueChange = { description = it }, placeholder = "e.g., Maintain 5m/km pace")

                Spacer(modifier = Modifier.height(16.dp))

                // Stat Type Selection
                Text("TARGET STAT", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (stat in StatType.values()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(if (selectedStat == stat) SystemBlue.copy(alpha = 0.2f) else Obsidian)
                                .border(1.dp, if (selectedStat == stat) SystemBlue else Color(0xFF1A2235))
                                .clickable { selectedStat = stat },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stat.name.take(3), color = if (selectedStat == stat) SystemBlue else Slate, fontSize = 12.sp, fontFamily = Rajdhani)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reward
                Text("REWARD AMOUNT", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    value = reward,
                    onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) reward = it },
                    placeholder = "10",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Frequency
                Text("FREQUENCY", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (freq in listOf(QuestFrequency.DAILY, QuestFrequency.ONCE)) {
                        val sel = selectedFrequency == freq
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(if (sel) SystemBlue.copy(alpha = 0.2f) else Obsidian)
                                .border(1.dp, if (sel) SystemBlue else Color(0xFF1A2235))
                                .clickable { selectedFrequency = freq },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(freq.name.take(6), color = if (sel) SystemBlue else Slate, fontSize = 10.sp, fontFamily = ShareTechMono)
                        }
                    }
                }

                if (selectedFrequency == QuestFrequency.ONCE) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("DEADLINE (HOURS)", color = SystemBlue, fontSize = 10.sp, fontFamily = ShareTechMono)
                    Spacer(modifier = Modifier.height(4.dp))
                    CustomTextField(
                        value = deadlineHoursText,
                        onValueChange = { if (it.isEmpty() || it.all { c -> c.isDigit() }) deadlineHoursText = it },
                        placeholder = "24",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Actions
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCEL", color = Slate, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && reward.isNotBlank()) {
                                val deadlineHours = if (selectedFrequency == QuestFrequency.ONCE) deadlineHoursText.toIntOrNull() ?: 24 else 0
                                onSave(title, description, selectedStat, reward.toIntOrNull() ?: 10, selectedFrequency, deadlineHours)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SystemBlue, contentColor = VoidBlack),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text("ACCEPT", fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, placeholder: String, keyboardOptions: KeyboardOptions = KeyboardOptions.Default) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = FrostWhite, fontSize = 16.sp, fontFamily = Outfit),
        cursorBrush = SolidColor(SystemBlue),
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VoidBlack)
                    .border(1.dp, Color(0xFF1A2235))
                    .padding(12.dp)
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, color = Slate.copy(alpha = 0.5f), fontSize = 16.sp, fontFamily = Outfit)
                }
                innerTextField()
            }
        }
    )
}
