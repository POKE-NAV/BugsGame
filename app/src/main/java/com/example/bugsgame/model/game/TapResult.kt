package com.example.bugsgame.model.game

sealed interface TapResult {
    data class Hit(
        val bug: Bug,
        val points: Int
    ): TapResult

    data object Miss: TapResult
    data object Ignored: TapResult
}
