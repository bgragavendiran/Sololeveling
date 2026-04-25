package com.heptre.sololeveling.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.QuestType
import com.heptre.sololeveling.data.QuestFrequency
import kotlinx.coroutines.launch

@Database(entities = [PlayerEntity::class, QuestEntity::class, ProgressionEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SoloLevelingDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun questDao(): QuestDao
    abstract fun progressionDao(): ProgressionDao

    companion object {
        @Volatile
        private var INSTANCE: SoloLevelingDatabase? = null

        // Migration from DB version 1 to 2 — add new quest columns
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE quests ADD COLUMN questType TEXT NOT NULL DEFAULT 'CUSTOM'")
                database.execSQL("ALTER TABLE quests ADD COLUMN frequency TEXT NOT NULL DEFAULT 'DAILY'")
                database.execSQL("ALTER TABLE quests ADD COLUMN targetValue INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE quests ADD COLUMN currentValue INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE quests ADD COLUMN failureDeadlineMs INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE quests ADD COLUMN isSystemQuest INTEGER NOT NULL DEFAULT 0")
            }
        }

        // Migration from DB version 2 to 3 — add progression tracking table
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS progression (
                        playerId INTEGER PRIMARY KEY NOT NULL,
                        currentStreak INTEGER NOT NULL DEFAULT 0,
                        lastCompletionDate INTEGER NOT NULL DEFAULT 0,
                        weeklyQuestCount INTEGER NOT NULL DEFAULT 0,
                        weeksCompleted INTEGER NOT NULL DEFAULT 0,
                        bestStreak INTEGER NOT NULL DEFAULT 0,
                        totalExp INTEGER NOT NULL DEFAULT 0,
                        lastUpdatedDate INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
                // Seed initial progression
                database.execSQL("""
                    INSERT OR IGNORE INTO progression (playerId, currentStreak, lastCompletionDate, weeklyQuestCount, weeksCompleted, bestStreak, totalExp, lastUpdatedDate)
                    VALUES (1, 0, 0, 0, 0, 0, 0, 0)
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context, scope: kotlinx.coroutines.CoroutineScope): SoloLevelingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoloLevelingDatabase::class.java,
                    "solo_leveling_system_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: kotlinx.coroutines.CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                scope.launch(kotlinx.coroutines.Dispatchers.IO) {
                    val playerDao = INSTANCE?.playerDao()
                    playerDao?.insertOrUpdatePlayer(
                        PlayerEntity(
                            id = 1,
                            name = "",
                            title = "UNRANKED",
                            rank = Rank.UNRANKED,
                            goldCurrent = 0,
                            goldTarget = 1000,
                            str = 10,
                            apt = 10,
                            int = 10,
                            end = 10,
                            cycleStartDate = System.currentTimeMillis()
                        )
                    )
                    seedSystemQuests(INSTANCE?.questDao())
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                scope.launch(kotlinx.coroutines.Dispatchers.IO) {
                    seedSystemQuestsIfNeeded(INSTANCE?.questDao())
                }
            }
        }
    }
}

/** Seed system quests on new DB creation. */
private suspend fun seedSystemQuests(questDao: QuestDao?) {
    if (questDao == null) return
    val now = System.currentTimeMillis()

    questDao.insertQuestIfNotExists(QuestEntity(
        id = 100,
        title = "DAILY STEPS PROTOCOL",
        description = "Achieve 10,000 steps by end of day (tracked via Health Connect)",
        statType = StatType.ENDURANCE,
        reward = 20,
        isCompleted = false,
        isAutoSync = true,
        isSkipped = false,
        createdDate = now,
        lastUpdatedDate = now,
        questType = QuestType.STEPS,
        frequency = QuestFrequency.DAILY,
        targetValue = 10000,
        currentValue = 0,
        failureDeadlineMs = 0L,
        isSystemQuest = true
    ))

    questDao.insertQuestIfNotExists(QuestEntity(
        id = 101,
        title = "MORNING PHYSICAL PROTOCOL",
        description = "25 push-ups, 25 squats, 25 jumping jacks, 25 mountain climbers",
        statType = StatType.STRENGTH,
        reward = 30,
        isCompleted = false,
        isAutoSync = false,
        isSkipped = false,
        createdDate = now,
        lastUpdatedDate = now,
        questType = QuestType.EXERCISE,
        frequency = QuestFrequency.BIWEEKLY,
        targetValue = 100,
        currentValue = 0,
        failureDeadlineMs = 0L,
        isSystemQuest = true
    ))

    questDao.insertQuestIfNotExists(QuestEntity(
        id = 102,
        title = "EVENING PHYSICAL PROTOCOL",
        description = "25 push-ups, 25 squats, 25 jumping jacks, 25 mountain climbers",
        statType = StatType.STRENGTH,
        reward = 30,
        isCompleted = false,
        isAutoSync = false,
        isSkipped = false,
        createdDate = now,
        lastUpdatedDate = now,
        questType = QuestType.EXERCISE,
        frequency = QuestFrequency.BIWEEKLY,
        targetValue = 100,
        currentValue = 0,
        failureDeadlineMs = 0L,
        isSystemQuest = true
    ))

    questDao.insertQuestIfNotExists(QuestEntity(
        id = 103,
        title = "NEURAL ENHANCEMENT PROTOCOL",
        description = "Complete 2 hours of study (Duolingo, books, courses)",
        statType = StatType.INTELLIGENCE,
        reward = 25,
        isCompleted = false,
        isAutoSync = false,
        isSkipped = false,
        createdDate = now,
        lastUpdatedDate = now,
        questType = QuestType.STUDY,
        frequency = QuestFrequency.DAILY,
        targetValue = 120,
        currentValue = 0,
        failureDeadlineMs = 0L,
        isSystemQuest = true
    ))

    questDao.insertQuestIfNotExists(QuestEntity(
        id = 104,
        title = "WEEKLY GYM PROTOCOL",
        description = "Complete 3 gym sessions of at least 1 hour each",
        statType = StatType.STRENGTH,
        reward = 50,
        isCompleted = false,
        isAutoSync = false,
        isSkipped = false,
        createdDate = now,
        lastUpdatedDate = now,
        questType = QuestType.GYM,
        frequency = QuestFrequency.WEEKLY_3X,
        targetValue = 3,
        currentValue = 0,
        failureDeadlineMs = 0L,
        isSystemQuest = true
    ))
}

/** Idempotent seeding — only insert if no system quests exist yet (e.g., after migration). */
private suspend fun seedSystemQuestsIfNeeded(questDao: QuestDao?) {
    if (questDao == null) return
    if (questDao.getSystemQuestCount() > 0) return
    seedSystemQuests(questDao)
}