package com.heptre.sololeveling.ui.rank_evaluation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.data.db.PlayerDao
import com.heptre.sololeveling.data.db.QuestDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class CycleStats(
    val completedQuests: Int,
    val totalQuests: Int,
    val penalties: Int = 0
) {
    val consistencyPercent: Int
        get() = if (totalQuests > 0) (completedQuests * 100) / totalQuests else 0
}

class RankEvaluationViewModel(
    private val playerDao: PlayerDao,
    private val questDao: QuestDao
) : ViewModel() {

    private val _currentRank = MutableStateFlow(Rank.UNRANKED)
    val currentRank: StateFlow<Rank> = _currentRank.asStateFlow()

    private val _nextRank = MutableStateFlow<Rank?>(Rank.K)
    val nextRank: StateFlow<Rank?> = _nextRank.asStateFlow()

    private val _cycleProgress = MutableStateFlow(0f)
    val cycleProgress: StateFlow<Float> = _cycleProgress.asStateFlow()

    private val _cycleNumber = MutableStateFlow(1)
    val cycleNumber: StateFlow<Int> = _cycleNumber.asStateFlow()

    private val _cycleStats = MutableStateFlow(CycleStats(0, 0))
    val cycleStats: StateFlow<CycleStats> = _cycleStats.asStateFlow()

    // Stat bonuses applied on rank claim
    val rankBonuses = mapOf("STR" to 15, "APT" to 10, "INT" to 5, "END" to 8)

    // true when the rank has just been claimed — screen observes this to navigate away
    private val _rankClaimed = MutableStateFlow(false)
    val rankClaimed: StateFlow<Boolean> = _rankClaimed.asStateFlow()

    init {
        viewModelScope.launch {
            playerDao.getPlayerState().collect { player ->
                if (player != null) {
                    _currentRank.value = player.rank

                    val ranks = Rank.values()
                    val rankIndex = ranks.indexOf(player.rank)
                    _nextRank.value = if (rankIndex < ranks.lastIndex) ranks[rankIndex + 1] else null

                    val cycleDurationMs = 21L * 24 * 60 * 60 * 1000
                    val timePassed = System.currentTimeMillis() - player.cycleStartDate
                    _cycleProgress.value = (timePassed.toFloat() / cycleDurationMs).coerceIn(0f, 1f)

                    // Cycle number: how many 21-day cycles since app start (approx)
                    _cycleNumber.value = ((timePassed / cycleDurationMs) + 1).toInt().coerceAtLeast(1)

                    // Quest completion stats since cycle start
                    val completed = questDao.getCompletedQuestCountSince(player.cycleStartDate)
                    val total = questDao.getTotalQuestCountSince(player.cycleStartDate)
                    _cycleStats.value = CycleStats(completed, total)
                }
            }
        }
    }

    fun claimNewRank() {
        viewModelScope.launch {
            val player = playerDao.getPlayerState().first() ?: return@launch
            val ranks = Rank.values()
            val currentIndex = ranks.indexOf(player.rank)
            if (currentIndex < ranks.lastIndex) {
                val newRank = ranks[currentIndex + 1]
                playerDao.updateRankAndCycle(newRank.name, System.currentTimeMillis())
                playerDao.addStats(
                    str = rankBonuses["STR"] ?: 0,
                    apt = rankBonuses["APT"] ?: 0,
                    intel = rankBonuses["INT"] ?: 0,
                    endur = rankBonuses["END"] ?: 0
                )
            }
            _rankClaimed.value = true
        }
    }
}
