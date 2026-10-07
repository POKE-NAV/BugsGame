package com.example.bugsgame.model

import java.time.LocalDate

data class Player(
    val id: Long = 0,
    val fio: String = "",
    val gender: String = "",
    val course: String = "",
    val difficultyIndex: Int = 0,
    val birthDate: LocalDate? = null,
)