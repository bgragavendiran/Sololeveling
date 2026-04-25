package com.heptre.sololeveling.data.progression

import com.heptre.sololeveling.data.db.QuestEntity

object ProgressionSystem {
    const val EXP_PER_STAT_POINT = 100
    const val STATS_FOR_LEVEL = 10

    fun calculateQuestExp(quest: QuestEntity, currentStreak: Int): Int {
        val baseExp = quest.reward * 10
        val multiplier = when (currentStreak) {
            in 0..1 -> 1.0f
            2 -> 1.0f
            3 -> 1.2f
            4 -> 1.2f
            5 -> 1.5f
            6 -> 1.5f
            else -> 2.0f
        }
        return (baseExp * multiplier).toInt()
    }

    fun processExperience(currentExp: Int, gainedExp: Int): Pair<Int, Int> {
        val totalExp = currentExp + gainedExp
        val statsGained = totalExp / EXP_PER_STAT_POINT
        val remainingExp = totalExp % EXP_PER_STAT_POINT
        return Pair(statsGained, remainingExp)
    }

    fun checkLevelUp(currentLevel: Int, totalStats: Int): Pair<Int, Int> {
        val statsNeeded = currentLevel * STATS_FOR_LEVEL
        val newLevel = (totalStats / STATS_FOR_LEVEL) + 1
        val remainingStats = totalStats % STATS_FOR_LEVEL
        return Pair(newLevel, remainingStats)
    }
}
