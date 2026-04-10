package com.heptre.sololeveling.ui.active_zone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActiveZoneViewModel : ViewModel() {
    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    private val _timerValue = MutableStateFlow(25 * 60) // 25 minutes in seconds
    val timerValue: StateFlow<Int> = _timerValue.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _hasFailed = MutableStateFlow(false)
    val hasFailed: StateFlow<Boolean> = _hasFailed.asStateFlow()

    private val _hasCompleted = MutableStateFlow(false)
    val hasCompleted: StateFlow<Boolean> = _hasCompleted.asStateFlow()

    private val _shouldAbort = MutableStateFlow(false)
    val shouldAbort: StateFlow<Boolean> = _shouldAbort.asStateFlow()

    fun startQuest() {
        viewModelScope.launch {
            _isActive.value = true
            _isRunning.value = true
            _hasFailed.value = false
            _hasCompleted.value = false
            
            while (_isRunning.value && _timerValue.value > 0) {
                delay(1000)
                _timerValue.value = _timerValue.value - 1
            }

            if (_timerValue.value == 0) {
                _isRunning.value = false
                _hasCompleted.value = true
            } else {
                _isRunning.value = false
                _hasFailed.value = true
                _shouldAbort.value = true
            }
        }
    }

    fun pauseQuest() {
        _isRunning.value = false
    }

    fun abortQuest() {
        _isRunning.value = false
        _hasFailed.value = true
        _shouldAbort.value = true
    }

    fun resetQuest() {
        _isActive.value = false
        _isRunning.value = false
        _timerValue.value = 25 * 60
        _hasFailed.value = false
        _hasCompleted.value = false
        _shouldAbort.value = false
    }

    fun acceptPenalty() {
        _hasFailed.value = false
        _shouldAbort.value = false
        resetQuest()
    }

    fun dismissPenalty() {
        _hasFailed.value = false
    }
}