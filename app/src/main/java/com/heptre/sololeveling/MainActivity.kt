package com.heptre.sololeveling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.heptre.sololeveling.ui.active_zone.ActiveZoneScreen
import com.heptre.sololeveling.ui.active_zone.ActiveZoneViewModel
import com.heptre.sololeveling.ui.historical_records.HistoricalRecordsScreen
import com.heptre.sololeveling.ui.initialize_system.InitializeSystemScreen
import com.heptre.sololeveling.ui.quest_log.QuestLogScreen
import com.heptre.sololeveling.ui.quest_log.QuestLogViewModel
import com.heptre.sololeveling.ui.rank_evaluation.RankEvaluationScreen
import com.heptre.sololeveling.ui.rank_evaluation.RankEvaluationViewModel
import com.heptre.sololeveling.ui.status_window.StatusWindowScreen
import com.heptre.sololeveling.ui.status_window.StatusWindowViewModel
import com.heptre.sololeveling.ui.system_config.SystemConfigScreen
import com.heptre.sololeveling.ui.system_config.SystemConfigViewModel
import com.heptre.sololeveling.ui.system_penalty.SystemPenaltyOverlay
import com.heptre.sololeveling.ui.theme.SololevelingTheme
import com.heptre.sololeveling.ui.SystemScaffold
import com.heptre.sololeveling.data.db.SoloLevelingDatabase
import com.heptre.sololeveling.data.health.HealthConnectManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

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
    val context = LocalContext.current
    val database = remember { SoloLevelingDatabase.getDatabase(context) }
    val healthManager = remember { HealthConnectManager(context) }

    // Health Connect Permission Setup
    val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
    val requestPermissions = androidx.activity.compose.rememberLauncherForActivityResult(requestPermissionActivityContract) { granted ->
        // Could trigger an initial sync here if necessary
    }

    LaunchedEffect(Unit) {
        if (HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE) {
            if (!healthManager.hasAllPermissions()) {
                requestPermissions.launch(healthManager.permissions)
            }
        }
    }

    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatusWindowViewModel::class.java)) {
                return StatusWindowViewModel(database.playerDao(), healthManager) as T
            }
            if (modelClass.isAssignableFrom(QuestLogViewModel::class.java)) {
                return QuestLogViewModel(database.questDao(), database.playerDao()) as T
            }
            if (modelClass.isAssignableFrom(ActiveZoneViewModel::class.java)) {
                return ActiveZoneViewModel() as T
            }
            if (modelClass.isAssignableFrom(RankEvaluationViewModel::class.java)) {
                return RankEvaluationViewModel(database.playerDao()) as T
            }
            if (modelClass.isAssignableFrom(SystemConfigViewModel::class.java)) {
                return SystemConfigViewModel(database.playerDao()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    val statusWindowViewModel: StatusWindowViewModel = viewModel(factory = factory)
    val questLogViewModel: QuestLogViewModel = viewModel(factory = factory)
    val activeZoneViewModel: ActiveZoneViewModel = viewModel(factory = factory)
    val rankEvaluationViewModel: RankEvaluationViewModel = viewModel(factory = factory)
    val systemConfigViewModel: SystemConfigViewModel = viewModel(factory = factory)

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute in listOf("status_window", "quest_log", "historical_records", "system_config")

    SystemScaffold(
        navController = navController,
        showBottomBar = showBottomBar
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "initialize_system",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("initialize_system") {
                InitializeSystemScreen(
                    onInitializeComplete = {
                        navController.navigate("status_window") {
                            popUpTo("initialize_system") { inclusive = true }
                        }
                    }
                )
            }

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

            composable("historical_records") {
                HistoricalRecordsScreen()
            }

            composable("system_config") {
                SystemConfigScreen(
                    viewModel = systemConfigViewModel
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
    }

    // System Penalty Overlay (Global)
    var hasFailed by remember { mutableStateOf(false) }
    if (hasFailed) {
        SystemPenaltyOverlay(
            onDismiss = { hasFailed = false },
            onAccept = { hasFailed = false }
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
