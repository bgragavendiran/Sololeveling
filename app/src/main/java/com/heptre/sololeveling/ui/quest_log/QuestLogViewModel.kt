package com.heptre.sololeveling.ui.quest_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.Quest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestLogViewModel : ViewModel() {
    private val _dailyQuests = MutableStateFlow<List<Quest>>(emptyList())
    val dailyQuests: StateFlow<List<Quest>> = _dailyQuests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadDailyQuests() {
        viewModelScope.launch {
            _isLoading.value = true
            // Simulate API call
            Thread.sleep(500)
            _dailyQuests.value = listOf(
                Quest(
                    id = 1,
                    title = "Cardio Workout",
                    description = "30 minutes of cardio",
                    statType = com.heptre.sololeveling.data.Quest.StatType.STRENGTH,
                    reward = 2,
                    isCompleted = false
                ),
                Quest(
                    id = 2,
                    title = "Strength Training",
                    description = "Heavy lifting session",
                    statType = com.heptre.sololeveling.data.Quest.StatType.STRENGTH,
                    reward = 3,
                    isCompleted = false
                ),
                Quest(
                    id = 3,
                    title = "Deep Work Session",
                    description = "Focus on learning task",
                    statType = com.heptre.sololeveling.data.Quest.StatType.APPTITUDE,
                    reward = 5,
                    isCompleted = false
                ),
                Quest(
                    id = 4,
                    title = "Read 30 Pages",
                    description = "Study a new topic",
                    statType = com.heptre.sololeveling.data.Quest.StatType.INTELLIGENCE,
                    reward = 2,
                    isCompleted = false
                ),
                Quest(
                    id = 5,
                    title = "Sleep Sync",
                    description = "Sync with Pixel Watch",
                    statType = com.heptre.sololeveling.data.Quest.StatType.ENDURANCE,
                    reward = 0,
                    isAutoSync = true
                )
            )
            _isLoading.value = false
        }
    }

    fun toggleQuestCompletion(quest: Quest) {
        viewModelScope.launch {
            val updatedQuests = _dailyQuests.value.map { currentQuest ->
                if (currentQuest.id == quest.id) {
                    currentQuest.copy(isCompleted = !currentQuest.isCompleted)
                } else {
                    currentQuest
                }
            }
            _dailyQuests.value = updatedQuests
        }
    }

    fun skipQuest(quest: Quest) {
        viewModelScope.launch {
            val updatedQuests = _dailyQuests.value.map { currentQuest ->
                if (currentQuest.id == quest.id) {
                    currentQuest.copy(isCompleted = false, isSkipped = true)
                } else {
                    currentQuest
                }
            }
            _dailyQuests.value = updatedQuests
        }
    }
}