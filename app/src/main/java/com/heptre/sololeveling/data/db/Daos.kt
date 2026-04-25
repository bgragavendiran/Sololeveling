package com.heptre.sololeveling.data.db


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_state WHERE id = 1")
    fun getPlayerState(): Flow<PlayerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlayer(player: PlayerEntity)

    @Query("SELECT name FROM player_state WHERE id = 1")
    suspend fun getPlayerName(): String?

    @Query("UPDATE player_state SET name = :name WHERE id = 1")
    suspend fun updatePlayerName(name: String)

    @Query("UPDATE player_state SET rank = :rankName, cycleStartDate = :cycleStartDate WHERE id = 1")
    suspend fun updateRankAndCycle(rankName: String, cycleStartDate: Long)

    @Query("UPDATE player_state SET str = str + :str, apt = apt + :apt, `int` = `int` + :intel, `end` = `end` + :endur WHERE id = 1")
    suspend fun addStats(str: Int, apt: Int, intel: Int, endur: Int)

    @Query("UPDATE player_state SET `apt` = `apt` + :amount WHERE id = 1")
    suspend fun addAptitude(amount: Int)

    @Query("UPDATE player_state SET `str` = `str` + :amount WHERE id = 1")
    suspend fun addStrength(amount: Int)

    @Query("UPDATE player_state SET `int` = `int` + :amount WHERE id = 1")
    suspend fun addIntelligence(amount: Int)

    @Query("UPDATE player_state SET `end` = `end` + :amount WHERE id = 1")
    suspend fun addEndurance(amount: Int)

    @Query("UPDATE player_state SET goldCurrent = goldCurrent + :amount WHERE id = 1")
    suspend fun addGold(amount: Int)

    @Query("UPDATE player_state SET goldCurrent = goldCurrent - :amount WHERE id = 1 AND goldCurrent >= :amount")
    suspend fun spendGold(amount: Int)
}

@Dao
interface QuestDao {
    @Query("SELECT * FROM quests ORDER BY createdDate DESC")
    fun getAllQuests(): Flow<List<QuestEntity>>

    @Query("SELECT * FROM quests WHERE isCompleted = 0 AND isSkipped = 0")
    fun getActiveQuests(): Flow<List<QuestEntity>>

    @Query("SELECT COUNT(*) FROM quests WHERE lastUpdatedDate >= :since AND isCompleted = 1")
    suspend fun getCompletedQuestCountSince(since: Long): Int

    @Query("SELECT COUNT(*) FROM quests WHERE createdDate >= :since")
    suspend fun getTotalQuestCountSince(since: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: QuestEntity)

    @Update
    suspend fun updateQuest(quest: QuestEntity)

    @Delete
    suspend fun deleteQuest(quest: QuestEntity)

    @Query("SELECT COUNT(*) FROM quests WHERE isSystemQuest = 1")
    suspend fun getSystemQuestCount(): Int

    @Query("SELECT * FROM quests WHERE isSystemQuest = 1")
    fun getSystemQuests(): Flow<List<QuestEntity>>

    @Query("SELECT * FROM quests WHERE questType = :type AND isSystemQuest = 1 LIMIT 1")
    suspend fun getSystemQuestByType(type: String): QuestEntity?

    @Query("SELECT * FROM quests WHERE id = :id")
    suspend fun getQuestById(id: Int): QuestEntity?

    @Query("UPDATE quests SET currentValue = :currentValue, isCompleted = :isCompleted, lastUpdatedDate = :timestamp WHERE id = :id")
    suspend fun updateQuestProgress(id: Int, currentValue: Int, isCompleted: Boolean, timestamp: Long)

    @Query("UPDATE quests SET isCompleted = 0, currentValue = 0, lastUpdatedDate = :timestamp WHERE isSystemQuest = 1 AND frequency = 'DAILY'")
    suspend fun resetDailySystemQuests(timestamp: Long)

    @Query("UPDATE quests SET isCompleted = 0, currentValue = 0, lastUpdatedDate = :timestamp WHERE isSystemQuest = 1 AND frequency = 'BIWEEKLY'")
    suspend fun resetBiweeklySystemQuests(timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestIfNotExists(quest: QuestEntity)
}

@Dao
interface ProgressionDao {
    @Query("SELECT * FROM progression WHERE playerId = 1")
    fun getProgression(): Flow<ProgressionEntity?>

    @Query("SELECT * FROM progression WHERE playerId = 1")
    suspend fun getProgressionSync(): ProgressionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgression(progression: ProgressionEntity)

    @Query("UPDATE progression SET currentStreak = :streak, lastCompletionDate = :date, bestStreak = :best WHERE playerId = 1")
    suspend fun updateStreak(streak: Int, date: Long, best: Int)

    @Query("UPDATE progression SET totalExp = totalExp + :exp, lastUpdatedDate = :date WHERE playerId = 1")
    suspend fun addExperience(exp: Int, date: Long)

    @Query("UPDATE progression SET weeklyQuestCount = weeklyQuestCount + :count WHERE playerId = 1")
    suspend fun addWeeklyQuests(count: Int)

    @Query("UPDATE progression SET weeksCompleted = weeksCompleted + 1 WHERE playerId = 1")
    suspend fun completeWeek()

    @Query("UPDATE progression SET currentStreak = 0, weeklyQuestCount = 0, lastUpdatedDate = :date WHERE playerId = 1")
    suspend fun resetWeekly(date: Long)
}
