package com.example.bugsgame.model.game

import kotlin.math.cos
import kotlin.math.sin

class Bug(
    val id: Long,                               //Уникальный идентификатор жука внутри текущего раунда
    val type: BugType,                          //Тип Жука
    val x: Float,                               //X-координата центра жука
    val y: Float,                               //Y-координата центра жука
    val directionRad: Float,                    //Направление движения в радианах
    val speed: Float,                           //Скорость жука
    val size: Float,                            //Размер жука
    val remainingLifetimeSec: Float? = null     //Остаток жизни в секундах
) {
    val radius: Float                           //Радиус жука
        get() = size / 2f

    val visualResId: Int                        //Ресурс типа жука
        get() = type.visualResId

    val vx: Float                               //Горизонтальная состовляющая скорости
        get() = cos(directionRad) * speed

    val vy: Float                               //Вертикальная состовляющая скорости
        get() = sin(directionRad) * speed
}