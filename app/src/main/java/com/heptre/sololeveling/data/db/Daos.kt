package com.heptre.sololeveling.data.db


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_state WHERE id = 1")
    fun getPlayerState(): Flow<PlayerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlayer(player: PlayerEntity)

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: QuestEntity)

    @Update
    suspend fun updateQuest(quest: QuestEntity)

    @Delete
    suspend fun deleteQuest(quest: QuestEntity)
}
