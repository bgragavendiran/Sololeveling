package com.heptre.sololeveling.ui.system_config

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.db.PlayerDao
import com.heptre.sololeveling.data.db.PlayerEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SystemConfigViewModel(
    private val playerDao: PlayerDao,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _playerState = MutableStateFlow<PlayerEntity?>(null)
    val playerState: StateFlow<PlayerEntity?> = _playerState.asStateFlow()

    private val _aptitudeTimerMinutes = MutableStateFlow(prefs.getInt(KEY_TIMER, 45))
    val aptitudeTimerMinutes: StateFlow<Int> = _aptitudeTimerMinutes.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean(KEY_NOTIFICATIONS, true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _aggressiveHudEnabled = MutableStateFlow(prefs.getBoolean(KEY_AGGRESSIVE_HUD, false))
    val aggressiveHudEnabled: StateFlow<Boolean> = _aggressiveHudEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            playerDao.getPlayerState().collect {
                _playerState.value = it
            }
        }
    }

    fun setAptitudeTimer(minutes: Int) {
        _aptitudeTimerMinutes.value = minutes
        prefs.edit().putInt(KEY_TIMER, minutes).apply()
    }

    fun setNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
    }

    fun setAggressiveHud(enabled: Boolean) {
        _aggressiveHudEnabled.value = enabled
        prefs.edit().putBoolean(KEY_AGGRESSIVE_HUD, enabled).apply()
    }

    companion object {
        const val KEY_TIMER = "aptitude_timer"
        const val KEY_NOTIFICATIONS = "notifications_enabled"
        const val KEY_AGGRESSIVE_HUD = "aggressive_hud_enabled"
        const val PREFS_NAME = "solo_leveling_prefs"
    }
}
