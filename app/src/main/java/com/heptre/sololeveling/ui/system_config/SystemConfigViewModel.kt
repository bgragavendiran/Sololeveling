package com.heptre.sololeveling.ui.system_config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.db.PlayerDao
import com.heptre.sololeveling.data.db.PlayerEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SystemConfigViewModel(private val playerDao: PlayerDao) : ViewModel() {
    private val _playerState = MutableStateFlow<PlayerEntity?>(null)
    val playerState: StateFlow<PlayerEntity?> = _playerState.asStateFlow()

    init {
        viewModelScope.launch {
            playerDao.getPlayerState().collect {
                _playerState.value = it
            }
        }
    }
}
