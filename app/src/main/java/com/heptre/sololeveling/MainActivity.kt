package com.heptre.sololeveling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.heptre.sololeveling.ui.active_zone.ActiveZoneViewModel
import com.heptre.sololeveling.ui.rank_evaluation.RankEvaluationViewModel
import com.heptre.sololeveling.ui.quest_log.QuestLogViewModel
import com.heptre.sololeveling.ui.status_window.StatusWindowViewModel
import com.heptre.sololeveling.ui.system_penalty.SystemPenaltyOverlay
import com.heptre.sololeveling.ui.theme.SololevelingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SololevelingTheme {
                NavigationGraph()
            }
        }
    }
}
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    val statusWindowViewModel = remember { StatusWindowViewModel() }
    val questLogViewModel = remember { QuestLogViewModel() }
    val activeZoneViewModel = remember { ActiveZoneViewModel() }
    val rankEvaluationViewModel = remember { RankEvaluationViewModel() }

    NavHost(
        navController = navController,
        startDestination = "status_window"
    ) {
        composable("status_window") {
            StatusWindowScreen(
                viewModel = statusWindowViewModel
            )
        }

        composable("quest_log") {
            QuestLogScreen(
                viewModel = questLogViewModel
            )
        }

        composable("active_zone") {
            ActiveZoneScreen(
                viewModel = activeZoneViewModel
            )
        }

        composable("rank_evaluation") {
            RankEvaluationScreen(
                viewModel = rankEvaluationViewModel
            )
        }
    }

    // System Penalty Overlay (Global)
    var hasFailed by remember { mutableStateOf(false) }
    SystemPenaltyOverlay(
        onDismiss = { hasFailed = false },
        onAccept = { hasFailed = false }
    )
}
@Composable
fun BottomNavigation(
    navController: NavController
) {
    NavigationBar {
        NavigationBarItem(
            selected = navController.currentDestination?.route == "status_window",
            onClick = { navController.navigate("status_window") },
            icon = { Icons.Default.Home },
            label = { Text("Status") }
        )

        NavigationBarItem(
            selected = navController.currentDestination?.route == "quest_log",
            onClick = { navController.navigate("quest_log") },
            icon = { Text("Quest") },
            label = { Text("Quest Log") }
        )

        NavigationBarItem(
            selected = navController.currentDestination?.route == "active_zone",
            onClick = { navController.navigate("active_zone") },
            icon = { Text("Active") },
            label = { Text("Active Zone") }
        )

        NavigationBarItem(
            selected = navController.currentDestination?.route == "rank_evaluation",
            onClick = { navController.navigate("rank_evaluation") },
            icon = { Text("Rank") },
            label = { Text("Rank Eval") }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SololevelingTheme {
        Greeting("Android")
    }
}
