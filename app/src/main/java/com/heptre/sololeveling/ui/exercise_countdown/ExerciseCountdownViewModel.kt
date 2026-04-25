package com.heptre.sololeveling.ui.exercise_countdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.db.PlayerDao
import com.heptre.sololeveling.data.db.QuestDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExerciseStep(val name: String, val reps: Int, val stat: StatType)

enum class ExercisePhase { INTRO, ACTIVE, REST, COMPLETE, FAILED }

class ExerciseCountdownViewModel(
    private val questId: Int,
    private val questDao: QuestDao,
    private val playerDao: PlayerDao
) : ViewModel() {

    val exercises = listOf(
        ExerciseStep("PUSH-UPS",          25, StatType.STRENGTH),
        ExerciseStep("SQUATS",            25, StatType.STRENGTH),
        ExerciseStep("JUMPING JACKS",     25, StatType.ENDURANCE),
        ExerciseStep("MOUNTAIN CLIMBERS", 25, StatType.ENDURANCE)
    )
    val totalSteps = exercises.size

    private val _phase            = MutableStateFlow(ExercisePhase.INTRO)
    val phase: StateFlow<ExercisePhase> = _phase.asStateFlow()

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _currentRep       = MutableStateFlow(exercises[0].reps)
    val currentRep: StateFlow<Int> = _currentRep.asStateFlow()

    private val _isRunning        = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _hasFailed        = MutableStateFlow(false)
    val hasFailed: StateFlow<Boolean> = _hasFailed.asStateFlow()

    private val _hasCompleted     = MutableStateFlow(false)
    val hasCompleted: StateFlow<Boolean> = _hasCompleted.asStateFlow()

    // Screen collects these and passes to TtsManager — keeps TTS out of ViewModel
    private val _ttsEvent = MutableSharedFlow<String>(extraBufferCapacity = 20)
    val ttsEvent: SharedFlow<String> = _ttsEvent.asSharedFlow()

    fun start() {
        if (_isRunning.value || _hasCompleted.value) return
        _isRunning.value = true
        _hasFailed.value = false
        _phase.value = ExercisePhase.ACTIVE
        viewModelScope.launch { runStep(0) }
    }

    private suspend fun runStep(index: Int) {
        if (!_isRunning.value) return

        val step = exercises[index]
        _currentStepIndex.value = index
        _currentRep.value = step.reps

        _ttsEvent.emit("Begin ${step.name}. ${step.reps} repetitions.")
        delay(2500)

        var rep = step.reps
        while (rep > 0 && _isRunning.value) {
            _currentRep.value = rep
            _ttsEvent.emit("$rep")
            delay(1800) // ~1.8 s per rep
            rep--
        }

        if (!_isRunning.value) return // abandoned mid-set

        _currentRep.value = 0
        _ttsEvent.emit("Complete.")

        if (index < exercises.lastIndex) {
            _phase.value = ExercisePhase.REST
            _ttsEvent.emit("Rest. Next: ${exercises[index + 1].name}")
            delay(5000) // 5-second rest between exercises
            if (_isRunning.value) {
                _phase.value = ExercisePhase.ACTIVE
                runStep(index + 1)
            }
        } else {
            _isRunning.value = false
            _hasCompleted.value = true
            _phase.value = ExercisePhase.COMPLETE
            _ttsEvent.emit("All protocols complete. Excellent performance, Hunter.")
            markQuestComplete()
        }
    }

    /** Called when the user navigates away or the screen pauses — instant failure. */
    fun onUserLeft() {
        if (_isRunning.value && !_hasCompleted.value) {
            _isRunning.value = false
            _hasFailed.value = true
            _phase.value = ExercisePhase.FAILED
            viewModelScope.launch {
                _ttsEvent.emit("Protocol abandoned. Failure recorded, Hunter.")
            }
        }
    }

    private fun markQuestComplete() {
        viewModelScope.launch {
            val quest = questDao.getQuestById(questId) ?: return@launch
            val totalReps = exercises.sumOf { it.reps }
            questDao.updateQuestProgress(questId, totalReps, true, System.currentTimeMillis())
            // Split reward between STR and END
            val half = (quest.reward / 2).coerceAtLeast(1)
            playerDao.addStrength(half)
            playerDao.addEndurance(half)
        }
    }
}
