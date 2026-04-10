package com.heptre.sololeveling.ui.status_window

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatusWindowViewModel : ViewModel() {
    private val _player = MutableStateFlow(Player())
    val player: StateFlow<Player> = _player.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isExpanded = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean> = _isExpanded.asStateFlow()

    fun loadPlayerStats() {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate API call
            Thread.sleep(1000)
            _player.value = Player(
                name = "Hunter",
                title = "J-Rank Hunter",
                rank = Rank.J,
                stats = Stats(strength = 50, aptitude = 30, intelligence = 40, endurance = 60),
                gold = Gold(4500, 10000)
            )
            _isLoading.value = false
        }
    }

    fun toggleStatExpansion() {
        _isExpanded.value = !_isExpanded.value
    }

    fun updateStatValue(statType: StatType, value: Int) {
        _player.value = _player.value.copy(
            stats = _player.value.stats.copy(
                when (statType) {
                    StatType.STRENGTH -> strength = value
                    StatType.APPTITUDE -> aptitude = value
                    StatType.INTELLIGENCE -> intelligence = value
                    StatType.ENDURANCE -> endurance = value
                }
            )
        )
    }
}