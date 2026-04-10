package com.heptre.sololeveling.data

data class Quest(
    val id: Int,
    val title: String,
    val description: String = "",
    val statType: StatType,
    val reward: Int = 0,
    val isCompleted: Boolean = false,
    val isAutoSync: Boolean = false,
    val isSkipped: Boolean = false
)

enum class StatType {
    STRENGTH, APPTITUDE, INTELLIGENCE, ENDURANCE
}