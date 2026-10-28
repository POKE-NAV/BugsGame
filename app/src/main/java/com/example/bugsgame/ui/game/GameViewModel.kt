package com.example.bugsgame.ui.game

import androidx.lifecycle.ViewModel
import com.example.bugsgame.data.SettingsRepository
import com.example.bugsgame.model.game.GameEngine
import com.example.bugsgame.model.game.GameEngineState
import com.example.bugsgame.model.game.TapResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel игрового экрана.
 *
 * Связывает три вещи:
 * - SettingsRepository — откуда берутся пользовательские настройки;
 * - GameEngine — вся игровая логика;
 * - StateFlow — через который UI получает состояние.
 *
 * ViewModel не рисует и не знает про пиксели.
 * Он принимает уже готовые логические координаты тапа.
 */
class GameViewModel(
    private val settingsRepository: SettingsRepository = SettingsRepository.shared,
) : ViewModel() {

    private val engine = GameEngine()

    private val _state = MutableStateFlow(engine.state)
    val state: StateFlow<GameEngineState> = _state.asStateFlow()

    fun startGame() {
        engine.startRound(settingsRepository.load())
        publishState()
    }

    fun restart() {
        startGame()
    }

    fun onFrame(dtSec: Float) {
        engine.update(dtSec)
        publishState()
    }

    fun onTap(x: Float, y: Float): TapResult {
        val result = engine.handleTap(x, y)
        publishState()
        return result
    }

    fun pause() {
        engine.pause()
        publishState()
    }

    fun resume() {
        engine.resume()
        publishState()
    }

    private fun publishState() {
        _state.value = engine.state
    }
}
