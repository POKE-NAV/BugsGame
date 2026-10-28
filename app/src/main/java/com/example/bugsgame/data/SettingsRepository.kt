package com.example.bugsgame.data

import com.example.bugsgame.model.GameSettings

class SettingsRepository {

    private var current: GameSettings = GameSettings()

    fun load(): GameSettings = current

    fun save(settings: GameSettings) {
        current = settings
    }

    fun reset(): GameSettings {
        current = GameSettings()
        return current
    }

    companion object {
        val shared: SettingsRepository = SettingsRepository()
    }
}
