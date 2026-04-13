package com.heptre.sololeveling.ui.rank_evaluation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.Rank
import com.heptre.sololeveling.data.db.PlayerDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankEvaluationViewModel(private val playerDao: PlayerDao) : ViewModel() {
    private val _currentRank = MutableStateFlow(Rank.UNRANKED)
    val currentRank: StateFlow<Rank> = _currentRank.asStateFlow()

    private val _nextRank = MutableStateFlow<Rank?>(Rank.J)
    val nextRank: StateFlow<Rank?> = _nextRank.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    init {
        viewModelScope.launch {
            playerDao.getPlayerState().collect { player ->
                if (player != null) {
                    _currentRank.value = player.rank
                    
                    // Basic progression logic based on rank indexing
                    val ranks = Rank.values()
                    val rankIndex = ranks.indexOf(player.rank)
                    
                    if (rankIndex < ranks.lastIndex) {
                        _nextRank.value = ranks[rankIndex + 1]
                    } else {
                        _nextRank.value = null // Reached max rank
                    }

                    // For now simulate progress based on cycles logic
                    val cycleDurationMs = 21L * 24 * 60 * 60 * 1000
                    val timePassed = System.currentTimeMillis() - player.cycleStartDate
                    val ratio = (timePassed.toFloat() / cycleDurationMs).coerceIn(0f, 1f)
                    _progress.value = ratio
                }
            }
        }
    }
}