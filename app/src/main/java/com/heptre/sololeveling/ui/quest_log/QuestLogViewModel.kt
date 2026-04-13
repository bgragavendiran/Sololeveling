package com.heptre.sololeveling.ui.quest_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.db.PlayerDao
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

    init {
        loadDailyQuests()
    }

    private fun loadDailyQuests() {
        viewModelScope.launch {
            questDao.getActiveQuests().collect { quests ->
                _dailyQuests.value = quests
                _isLoading.value = false
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

            if(newIsCompleted) {
                // Award points
                when(quest.statType) {
                    StatType.STRENGTH -> playerDao.addStrength(quest.reward)
                    StatType.APPTITUDE -> playerDao.addAptitude(quest.reward)
                    StatType.INTELLIGENCE -> playerDao.addIntelligence(quest.reward)
                    StatType.ENDURANCE -> playerDao.addEndurance(quest.reward)
                }
            } else {
                // Remove points
                when(quest.statType) {
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
            // Spending gold to skip
            playerDao.spendGold(100)
        }
    }
}