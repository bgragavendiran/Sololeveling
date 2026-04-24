package com.heptre.sololeveling.data.db

import androidx.room.*
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.data.StatType

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
    val lastUpdatedDate: Long
)

class Converters {
    @TypeConverter
    fun fromStatType(value: StatType) = value.name

    @TypeConverter
    fun toStatType(value: String) = enumValueOf<StatType>(value)

    @TypeConverter
    fun fromRank(value: Rank) = value.name

    @TypeConverter
    fun toRank(value: String) = enumValueOf<Rank>(value)
}
