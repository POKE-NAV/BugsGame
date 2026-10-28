package com.example.bugsgame.model.game

object GameField {

    //Виртуальная система координат(НЕ ПИКСЕЛИ)
    const val WIDTH = 1080f                             //Ширина поля
    const val HEIGHT = 1920f                            //Высота поля

    fun isInside(x: Float, y: Float): Boolean =         //Проверка на нахождение ли точка внутри поля
        x in 0f..WIDTH && y in 0f..HEIGHT

    fun clampX(x: Float, radius: Float): Float =        //Ограничение x координаты центра жука, что бы не выходил за границы
        x.coerceIn(radius, WIDTH - radius)

    fun clampY(y: Float, radius: Float): Float =        //Ограничение y координаты центра жука, что бы не выходил за границы
        y.coerceIn(radius, HEIGHT - radius)
}