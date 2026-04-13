package com.heptre.sololeveling.data.db

import android.content.Context

import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.heptre.sololeveling.data.Rank

@Database(entities = [PlayerEntity::class, QuestEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SoloLevelingDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun questDao(): QuestDao

    companion object {
        @Volatile
        private var INSTANCE: SoloLevelingDatabase? = null

        fun getDatabase(context: Context): SoloLevelingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoloLevelingDatabase::class.java,
                    "solo_leveling_system_db"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            // override for Room multi-platform versions
            override suspend fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
                INSTANCE?.let { database ->
                    val playerDao = database.playerDao()
                    // Initial Boot Values
                    playerDao.insertOrUpdatePlayer(
                        PlayerEntity(
                            id = 1,
                            name = "PLAYER",
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
                }
            }
        }
    }
}
