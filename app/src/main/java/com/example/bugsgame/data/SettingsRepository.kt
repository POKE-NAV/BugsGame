package com.example.bugsgame.data

import com.example.bugsgame.model.GameSettings

/**
 * Единственный источник истины для настроек игры.
 *
 * Скрывает чтение/сохранение/сброс — ViewModel не знает, где именно хранятся данные.
 * Пока настройки хранятся в памяти: на следующей лекции заменим на DataStore,
 * не меняя при этом ни ViewModel, ни Screen.
 */
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
}
