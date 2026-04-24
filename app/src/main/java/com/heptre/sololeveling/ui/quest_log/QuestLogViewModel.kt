package com.heptre.sololeveling.ui.quest_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.db.PlayerDao
import com.heptre.sololeveling.data.db.PlayerEntity
import com.heptre.sololeveling.data.db.QuestDao
import com.heptre.sololeveling.data.db.QuestEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestLogViewModel(
    private val questDao: QuestDao,
    private val playerDao: PlayerDao
) : ViewModel() {

    private val _dailyQuests = MutableStateFlow<List<QuestEntity>>(emptyList())
    val dailyQuests: StateFlow<List<QuestEntity>> = _dailyQuests.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _playerState = MutableStateFlow<PlayerEntity?>(null)
    val playerState: StateFlow<PlayerEntity?> = _playerState.asStateFlow()

    // 0–100 representing quest completion % for today's active list
    private val _syncRate = MutableStateFlow(0)
    val syncRate: StateFlow<Int> = _syncRate.asStateFlow()

    // Computed "level" from total stat points
    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level.asStateFlow()

    init {
        loadDailyQuests()
        loadPlayerState()
    }

    private fun loadDailyQuests() {
        viewModelScope.launch {
            questDao.getAllQuests().collect { allQuests ->
                val active = allQuests.filter { !it.isSkipped }
                _dailyQuests.value = active.filter { !it.isCompleted }

                val total = active.size
                val completed = active.count { it.isCompleted }
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

    fun addCustomQuest(title: String, description: String, statType: StatType, reward: Int) {
        viewModelScope.launch {
            val quest = QuestEntity(
                title = title,
                description = description,
                statType = statType,
                reward = reward,
                isCompleted = false,
                isAutoSync = false,
                isSkipped = false,
                createdDate = System.currentTimeMillis(),
                lastUpdatedDate = System.currentTimeMillis()
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
                when (quest.statType) {
                    StatType.STRENGTH -> playerDao.addStrength(quest.reward)
                    StatType.APPTITUDE -> playerDao.addAptitude(quest.reward)
                    StatType.INTELLIGENCE -> playerDao.addIntelligence(quest.reward)
                    StatType.ENDURANCE -> playerDao.addEndurance(quest.reward)
                }
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
