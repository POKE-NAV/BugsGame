package com.example.bugsgame.model.game

import com.example.bugsgame.R

enum class BugType(
    val points: Int,            //Очки за жука
    val baseSpeed: Float,       //Базовая скорость
    val size: Float,            //Размер жука
    val spawnWeight: Int,       //Вес при случайном выборе во время спавна
    val lifetimeSec: Float?,    //Время жизни
    val visualResId: Int        //Ресурс для отрисовки
) {
    NORMAL(
        points = 1,
        baseSpeed = 140f,
        size = 100f,
        spawnWeight = 70,
        lifetimeSec = null,
        visualResId = R.drawable.normal_background
    ),
    FAST(
        points = 3,
        baseSpeed = 280f,
        size = 75f,
        spawnWeight = 25,
        lifetimeSec = null,
        visualResId = R.drawable.fast_background
    ),
    RARE(
        points = 5,
        baseSpeed = 180f,
        size = 60f,
        spawnWeight = 5,
        lifetimeSec = 6f,
        visualResId = R.drawable.rare_background
    )
}