package com.heptre.sololeveling.ui.initialize_system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.db.PlayerDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InitializeSystemViewModel(private val playerDao: PlayerDao) : ViewModel() {

    // null = still loading, false = not initialized, true = already initialized
    private val _isInitialized = MutableStateFlow<Boolean?>(null)
    val isInitialized: StateFlow<Boolean?> = _isInitialized.asStateFlow()

    init {
        viewModelScope.launch {
            val name = playerDao.getPlayerName()
            _isInitialized.value = !name.isNullOrEmpty()
        }
    }

    fun initializePlayer(name: String) {
        viewModelScope.launch {
            playerDao.updatePlayerName(name)
            _isInitialized.value = true
        }
    }
}
