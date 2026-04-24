package com.heptre.sololeveling.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.heptre.sololeveling.data.Rank
import kotlinx.coroutines.launch

@Database(entities = [PlayerEntity::class, QuestEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SoloLevelingDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun questDao(): QuestDao

    companion object {
        @Volatile
        private var INSTANCE: SoloLevelingDatabase? = null

        fun getDatabase(context: Context, scope: kotlinx.coroutines.CoroutineScope): SoloLevelingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoloLevelingDatabase::class.java,
                    "solo_leveling_system_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: kotlinx.coroutines.CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
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
                }
            }
        }
    }
}