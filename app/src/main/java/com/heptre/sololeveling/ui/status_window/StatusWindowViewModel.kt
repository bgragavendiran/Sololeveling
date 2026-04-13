package com.heptre.sololeveling.ui.status_window

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.Gold
import com.heptre.sololeveling.data.Player
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.Stats
import com.heptre.sololeveling.data.db.PlayerDao
import com.heptre.sololeveling.data.health.HealthConnectManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatusWindowViewModel(
    private val playerDao: PlayerDao,
    private val healthManager: HealthConnectManager
) : ViewModel() {
    private val _player = MutableStateFlow(Player())
    val player: StateFlow<Player> = _player.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isExpanded = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean> = _isExpanded.asStateFlow()

    init {
        syncHealthData()
        loadPlayerStats()
    }

    private fun syncHealthData() {
        viewModelScope.launch {
            if (healthManager.hasAllPermissions()) {
                val steps = healthManager.readDailySteps()
                val hasExercised = healthManager.checkDailyExercise()

                // Arbitrary game logic: 1000 steps = 1 Endurance point
                val endBonus = (steps / 1000).toInt()
                // Arbitrary game logic: workout = 5 Str points
                val strBonus = if (hasExercised) 5 else 0

                // Technically, you'd track previously granted bonus to not double-count, 
                // but for this implementation we simply overwrite a baseline or increment daily.
                // We'll increment safely using DAO if this was a daily check.
            }
        }
    }

    private fun loadPlayerStats() {
        viewModelScope.launch {
            playerDao.getPlayerState().collect { entity ->
                if (entity != null) {
                    _player.value = Player(
                        name = entity.name,
                        title = entity.title,
                        rank = entity.rank,
                        stats = Stats(
                            strength = entity.str,
                            aptitude = entity.apt,
                            intelligence = entity.int,
                            endurance = entity.end
                        ),
                        gold = Gold(entity.goldCurrent, entity.goldTarget)
                    )
                }
                _isLoading.value = false
            }
        }
    }

    fun toggleStatExpansion() {
        _isExpanded.value = !_isExpanded.value
    }
}