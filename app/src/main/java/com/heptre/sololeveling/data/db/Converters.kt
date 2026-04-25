package com.heptre.sololeveling.data.db

import androidx.room.TypeConverter
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.data.StatType
import com.heptre.sololeveling.data.QuestType
import com.heptre.sololeveling.data.QuestFrequency

class Converters {
    @TypeConverter
    fun fromStatType(value: StatType) = value.name

    @TypeConverter
    fun toStatType(value: String) = enumValueOf<StatType>(value)

    @TypeConverter
    fun fromRank(value: Rank) = value.name

    @TypeConverter
    fun toRank(value: String) = enumValueOf<Rank>(value)

    @TypeConverter
    fun fromQuestType(value: QuestType) = value.name

    @TypeConverter
    fun toQuestType(value: String) = enumValueOf<QuestType>(value)

    @TypeConverter
    fun fromQuestFrequency(value: QuestFrequency) = value.name

    @TypeConverter
    fun toQuestFrequency(value: String) = enumValueOf<QuestFrequency>(value)
}
