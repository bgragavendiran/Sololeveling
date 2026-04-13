package com.heptre.sololeveling.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.heptre.sololeveling.ui.theme.ShareTechMono
import com.heptre.sololeveling.ui.theme.Slate
import com.heptre.sololeveling.ui.theme.SystemBlue
import com.heptre.sololeveling.ui.theme.VoidBlack

@Composable
fun SystemScaffold(
    navController: NavController,
    showBottomBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                ShadowMonarchNavigationBar(navController)
            }
        },
        containerColor = VoidBlack
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
fun ShadowMonarchNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // The core navigation wrapper
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(VoidBlack.copy(alpha = 0.8f)) // Simple fallback for Backdrop blur 
            .border(width = 0.5.dp, color = SystemBlue.copy(alpha = 0.3f))
            .shadow(
                elevation = 20.dp,
                spotColor = Color.Black.copy(alpha = 0.5f),
                ambientColor = Color.Black.copy(alpha = 0.5f)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavBarItem(
            label = "STATUS",
            // Since we don't have custom icons setup yet, we'll just use text representation for now
            // Or standard Compose icons if we know the exact ones.
            isSelected = currentRoute == "status_window",
            onClick = { navController.navigate("status_window") { launchSingleTop = true } }
        )
        NavBarItem(
            label = "QUESTS",
            isSelected = currentRoute == "quest_log",
            onClick = { navController.navigate("quest_log") { launchSingleTop = true } }
        )
        NavBarItem(
            label = "STATS",
            isSelected = currentRoute == "historical_records",
            onClick = { navController.navigate("historical_records") { launchSingleTop = true } }
        )
        NavBarItem(
            label = "CONFIG",
            isSelected = currentRoute == "system_config",
            onClick = { navController.navigate("system_config") { launchSingleTop = true } }
        )
    }
}

@Composable
private fun NavBarItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) SystemBlue else Slate
    val alpha = if (isSelected) 1f else 0.5f

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .alpha(alpha)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Top indicator for active state
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(2.dp)
                    .background(SystemBlue)
                    .offset(y = (-8).dp)
            )
        }

        // Placeholder for Icon
        Box(
            modifier = Modifier.size(24.dp).background(color.copy(alpha = 0.2f))
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontFamily = ShareTechMono,
            fontSize = 10.sp,
            color = color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            letterSpacing = 1.sp
        )
    }
}
