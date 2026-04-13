package com.heptre.sololeveling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heptre.sololeveling.ui.theme.*

class PermissionsRationaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SololevelingTheme {
                Box(modifier = Modifier.fillMaxSize().background(VoidBlack), contentAlignment = Alignment.Center) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SYSTEM ACCESS REQUIRED", color = SystemBlue, fontSize = 24.sp, fontFamily = Rajdhani, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "The system requires access to Google Health Connect to verify your physical exertion bounds (Endurance & Strength). Rejecting access may lead to inaccurate power level analysis and cycle failure.",
                            color = FrostWhite,
                            fontSize = 14.sp,
                            fontFamily = Outfit
                        )
                    }
                }
            }
        }
    }
}
