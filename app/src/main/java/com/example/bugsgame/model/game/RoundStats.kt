package com.example.bugsgame.model.game

data class RoundStats(
    val score: Int = 0,
    val hits: Int = 0,
    val misses: Int = 0
) {
    val totalTaps: Int
        get() = hits + misses

    val accuracyPercent: Double
        get() = if (totalTaps == 0) {
            0.0
        } else {
            hits * 100.0 / totalTaps
        }
}