package com.example.bugsgame.model.game

import com.example.bugsgame.model.GameSettings

data class GameEngineState(
    val phase: GamePhase = GamePhase.READY,         //Текущая фаза игры
    val bugs: List<Bug> = emptyList(),              //Все живые жуки на поле
    val stats: RoundStats = RoundStats(),           //Очки, попадания и промахи текущего раунда
    val remainingSec: Float = 0f,                   //Сколько секунд осталось до конца раунда
    val elapsedSec: Float = 0f,                     //Сколько секунд уже прошло с начала раунда
    val settings: GameSettings = GameSettings()     //Настройки, с которыми был начат текущий раунд.
) {

}