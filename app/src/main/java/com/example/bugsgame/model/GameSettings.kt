package com.example.bugsgame.model

data class GameSettings(
    val gameSpeed: Int = 2,
    val maxBugs: Int = 5,
    val bonusIntervalSec: Int = 15,
    val roundDurationSec: Int = 60,
)