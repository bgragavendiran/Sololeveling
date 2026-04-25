package com.heptre.sololeveling.ui.quest_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.QuestType
import com.heptre.sololeveling.data.QuestFrequency
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.db.PlayerDao
import com.heptre.sololeveling.data.db.PlayerEntity
import com.heptre.sololeveling.data.db.ProgressionDao
import com.heptre.sololeveling.data.db.QuestDao
import com.heptre.sololeveling.data.db.QuestEntity
import com.heptre.sololeveling.data.health.HealthConnectManager
import com.heptre.sololeveling.data.progression.ProgressionSystem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestLogViewModel(
    private val questDao: QuestDao,
    private val playerDao: PlayerDao,
    private val progressionDao: ProgressionDao,
    private val healthManager: HealthConnectManager? = null
) : ViewModel() {

    // System quests (always shown)
    private val _systemQuests = MutableStateFlow<List<QuestEntity>>(emptyList())
    val systemQuests: StateFlow<List<QuestEntity>> = _systemQuests.asStateFlow()

    // User-created quests (shown if not completed)
    private val _customQuests = MutableStateFlow<List<QuestEntity>>(emptyList())
    val customQuests: StateFlow<List<QuestEntity>> = _customQuests.asStateFlow()

    // For backwards compatibility
    private val _dailyQuests = MutableStateFlow<List<QuestEntity>>(emptyList())
    val dailyQuests: StateFlow<List<QuestEntity>> = _dailyQuests.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _playerState = MutableStateFlow<PlayerEntity?>(null)
    val playerState: StateFlow<PlayerEntity?> = _playerState.asStateFlow()

    private val _syncRate = MutableStateFlow(0)
    val syncRate: StateFlow<Int> = _syncRate.asStateFlow()

    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level.asStateFlow()

    private val _ttsEvent = MutableSharedFlow<String>()
    val ttsEvent: SharedFlow<String> = _ttsEvent.asSharedFlow()

    init {
        loadAllQuests()
        loadPlayerState()
        syncSteps()
    }

    private suspend fun announceTts(message: String) {
        _ttsEvent.emit(message)
    }

    private fun loadAllQuests() {
        viewModelScope.launch {
            questDao.getAllQuests().collect { allQuests ->
                val system = allQuests.filter { it.isSystemQuest }.sortedBy { it.id }
                val custom = allQuests.filter { !it.isSystemQuest && !it.isCompleted && !it.isSkipped }

                _systemQuests.value = system
                _customQuests.value = custom
                _dailyQuests.value = custom // backwards compat

                val total = (system + custom).filter { !it.isSkipped }.size
                val completed = (system + custom).count { it.isCompleted && !it.isSkipped }
                _syncRate.value = if (total > 0) (completed * 100) / total else 0

                _isLoading.value = false
            }
        }
    }

    private fun loadPlayerState() {
        viewModelScope.launch {
            playerDao.getPlayerState().collect { player ->
                _playerState.value = player
                if (player != null) {
                    _level.value = ((player.str + player.apt + player.int + player.end) / 5).coerceAtLeast(1)
                }
            }
        }
    }

    fun syncSteps() {
        viewModelScope.launch {
            try {
                if (healthManager == null) return@launch
                val currentSteps = healthManager.readDailySteps()
                val stepsQuest = questDao.getQuestById(100) ?: return@launch
                val isComplete = currentSteps >= stepsQuest.targetValue

                questDao.updateQuestProgress(
                    100,
                    currentSteps.toInt().coerceAtMost(stepsQuest.targetValue),
                    isComplete,
                    System.currentTimeMillis()
                )

                if (isComplete && !stepsQuest.isCompleted) {
                    playerDao.addEndurance(stepsQuest.reward)
                }
            } catch (e: Exception) {
                // Health Connect not available or permission denied
            }
        }
    }

    fun logStudySession(minutes: Int) {
        viewModelScope.launch {
            val studyQuest = questDao.getQuestById(103) ?: return@launch
            val newValue = (studyQuest.currentValue + minutes).coerceAtMost(studyQuest.targetValue)
            val isComplete = newValue >= studyQuest.targetValue

            questDao.updateQuestProgress(103, newValue, isComplete, System.currentTimeMillis())

            announceTts("Logged $minutes minutes. Progress: $newValue / ${studyQuest.targetValue}")

            if (isComplete && !studyQuest.isCompleted) {
                playerDao.addIntelligence(studyQuest.reward)
                announceTts("Neural Enhancement Protocol complete. Intelligence increased.")
                processQuestCompletion(studyQuest)
            }
        }
    }

    fun logGymSession() {
        viewModelScope.launch {
            val gymQuest = questDao.getQuestById(104) ?: return@launch
            val newValue = (gymQuest.currentValue + 1).coerceAtMost(gymQuest.targetValue)
            val isComplete = newValue >= gymQuest.targetValue

            questDao.updateQuestProgress(104, newValue, isComplete, System.currentTimeMillis())

            announceTts("Gym session logged. Progress: $newValue / ${gymQuest.targetValue}")

            if (isComplete && !gymQuest.isCompleted) {
                playerDao.addStrength(gymQuest.reward)
                announceTts("Weekly Gym Protocol complete. Strength increased.")
                processQuestCompletion(gymQuest)
            }
        }
    }

    fun addCustomQuest(
        title: String,
        description: String,
        statType: StatType,
        reward: Int,
        frequency: QuestFrequency = QuestFrequency.DAILY,
        deadlineHours: Int = 0
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val deadline = if (deadlineHours > 0) now + (deadlineHours * 60 * 60 * 1000L) else 0L

            val quest = QuestEntity(
                title = title,
                description = description,
                statType = statType,
                reward = reward,
                isCompleted = false,
                isAutoSync = false,
                isSkipped = false,
                createdDate = now,
                lastUpdatedDate = now,
                questType = QuestType.CUSTOM,
                frequency = frequency,
                targetValue = 0,
                currentValue = 0,
                failureDeadlineMs = deadline,
                isSystemQuest = false
            )
            questDao.insertQuest(quest)
        }
    }

    fun toggleQuestCompletion(quest: QuestEntity) {
        viewModelScope.launch {
            val newIsCompleted = !quest.isCompleted
            questDao.updateQuest(quest.copy(
                isCompleted = newIsCompleted,
                lastUpdatedDate = System.currentTimeMillis()
            ))

            if (newIsCompleted) {
                processQuestCompletion(quest)
            } else {
                when (quest.statType) {
                    StatType.STRENGTH -> playerDao.addStrength(-quest.reward)
                    StatType.APPTITUDE -> playerDao.addAptitude(-quest.reward)
                    StatType.INTELLIGENCE -> playerDao.addIntelligence(-quest.reward)
                    StatType.ENDURANCE -> playerDao.addEndurance(-quest.reward)
                }
            }
        }
    }

    private suspend fun processQuestCompletion(quest: QuestEntity) {
        val progression = progressionDao.getProgressionSync()
        val now = System.currentTimeMillis()

        val exp = ProgressionSystem.calculateQuestExp(quest, progression?.currentStreak ?: 0)
        val (statsGained, remainingExp) = ProgressionSystem.processExperience(progression?.totalExp ?: 0, exp)

        when (quest.statType) {
            StatType.STRENGTH -> playerDao.addStrength(quest.reward + statsGained)
            StatType.APPTITUDE -> playerDao.addAptitude(quest.reward + statsGained)
            StatType.INTELLIGENCE -> playerDao.addIntelligence(quest.reward + statsGained)
            StatType.ENDURANCE -> playerDao.addEndurance(quest.reward + statsGained)
        }

        val updatedProgression = progression?.copy(totalExp = remainingExp) ?: com.heptre.sololeveling.data.db.ProgressionEntity()
        progressionDao.updateProgression(updatedProgression)

        if (statsGained > 0) {
            announceTts("Gained $statsGained stat points")
        }

        if (progression != null && statsGained > 0) {
            val totalStats = (progression.currentStreak * ProgressionSystem.STATS_FOR_LEVEL) + statsGained
            val (newLevel, _) = ProgressionSystem.checkLevelUp(1, totalStats)
            if (newLevel > 1) {
                announceTts("LEVEL UP! You are now level $newLevel")
            }
        }
    }

    fun skipQuest(quest: QuestEntity) {
        viewModelScope.launch {
            questDao.updateQuest(quest.copy(
                isSkipped = true,
                lastUpdatedDate = System.currentTimeMillis()
            ))
            playerDao.spendGold(100)
        }
    }
}
