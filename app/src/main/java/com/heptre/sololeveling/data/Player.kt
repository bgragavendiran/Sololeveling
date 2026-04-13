package com.heptre.sololeveling.data

data class Player(
    val name: String = "Player",
    val title: String = "Unranked",
    val rank: Rank = Rank.UNRANKED,
    val stats: Stats = Stats(),
    val gold: Gold = Gold()
)

enum class Rank {
    UNRANKED, K, J, I, H, G, F, E, D, C, B, A, S
}

data class Stats(
    val strength: Int = 0,
    val aptitude: Int = 0,
    val intelligence: Int = 0,
    val endurance: Int = 0
)
