package com.heptre.sololeveling.ui.rank_evaluation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heptre.sololeveling.data.Rank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RankEvaluationViewModel : ViewModel() {
    private val _currentRank = MutableStateFlow(Rank.J)
    val currentRank: StateFlow<Rank> = _currentRank.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _nextRank = MutableStateFlow<Rank?>(null)
    val nextRank: StateFlow<Rank?> = _nextRank.asStateFlow()

    fun evaluateRank(rank: Rank) {
        viewModelScope.launch {
            _currentRank.value = rank
            _progress.value = when (rank) {
                Rank.UNRANKED -> 0f
                Rank.J -> 0.1f
                Rank.I -> 0.2f
                Rank.H -> 0.3f
                Rank.G -> 0.4f
                Rank.F -> 0.5f
                Rank.E -> 0.6f
                Rank.D -> 0.7f
                Rank.C -> 0.8f
                Rank.B -> 0.9f
                Rank.A -> 1.0f
                Rank.S -> 1.0f
            }

            // Determine next rank
            _nextRank.value = when (rank) {
                Rank.UNRANKED -> Rank.J
                Rank.J -> Rank.I
                Rank.I -> Rank.H
                Rank.H -> Rank.G
                Rank.G -> Rank.F
                Rank.F -> Rank.E
                Rank.E -> Rank.D
                Rank.D -> Rank.C
                Rank.C -> Rank.B
                Rank.B -> Rank.A
                Rank.A -> Rank.S
                Rank.S -> null
            }
        }
    }
}