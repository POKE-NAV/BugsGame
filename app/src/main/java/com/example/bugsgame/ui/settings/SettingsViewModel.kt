package com.example.bugsgame.ui.settings

import androidx.lifecycle.ViewModel
import com.example.bugsgame.data.SettingsRepository
import com.example.bugsgame.model.GameSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val repository: SettingsRepository = SettingsRepository.shared,
) : ViewModel() {

    private val _settings = MutableStateFlow(repository.load())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    fun onGameSpeedChanged(value: Int) = update { it.copy(gameSpeed = value) }

    fun onMaxBugsChanged(value: Int) = update { it.copy(maxBugs = value.coerceIn(1, 20)) }

    fun onBonusIntervalChanged(value: Int) = update { it.copy(bonusIntervalSec = value) }

    fun onRoundDurationChanged(value: Int) = update { it.copy(roundDurationSec = value) }

    fun onReset() {
        _settings.value = repository.reset()
    }

    private fun update(transform: (GameSettings) -> GameSettings) {
        _settings.update { current ->
            val updated = transform(current)
            repository.save(updated)
            updated
        }
    }
}
