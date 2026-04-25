package com.heptre.sololeveling.data.db

import androidx.room.*
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.QuestType
import com.heptre.sololeveling.data.QuestFrequency

@Entity(tableName = "player_state")
data class PlayerEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val title: String,
    val rank: Rank,
    val goldCurrent: Int,
    val goldTarget: Int,
    val str: Int,
    val apt: Int,
    val int: Int,
    val end: Int,
    val cycleStartDate: Long
)

@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val statType: StatType,
    val reward: Int,
    val isCompleted: Boolean,
    val isAutoSync: Boolean,
    val isSkipped: Boolean,
    val createdDate: Long,
    val lastUpdatedDate: Long,
    val questType: QuestType = QuestType.CUSTOM,
    val frequency: QuestFrequency = QuestFrequency.DAILY,
    val targetValue: Int = 0,
    val currentValue: Int = 0,
    val failureDeadlineMs: Long = 0L,
    val isSystemQuest: Boolean = false
)

@Entity(tableName = "progression")
data class ProgressionEntity(
    @PrimaryKey val playerId: Int = 1,
    val currentStreak: Int = 0,
    val lastCompletionDate: Long = 0,
    val weeklyQuestCount: Int = 0,
    val weeksCompleted: Int = 0,
    val bestStreak: Int = 0,
    val totalExp: Int = 0,
    val lastUpdatedDate: Long = System.currentTimeMillis()
)
