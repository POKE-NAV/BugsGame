package com.example.bugsgame.model.game

object GameConfig {
    const val INITIAL_BUGS: Int = 3                 //Сколько жуков создать в начале раунда
    const val SPAWN_INTERVAL_SEC: Float = 0.8f      //Интервал обычного спавна
    const val MAX_FRAME_DT_SEC: Float = 0.1f        //Максимальный dt одного кадра
    const val TAP_TOLERANCE_LOGICAL: Float = 20f    //Дополнительный допуск при проверке попадания, в логических единицах
    const val MISS_PENALTY: Int = 1                 //Штраф за промах
    const val MIN_SCORE: Int = 0                    //Минимальное значение счёта
    const val MAX_RARE_ON_FIELD: Int = 1            //Максимум редких жуков на поле одновременно
    const val SPAWN_MARGIN: Float = 24f             //Отступ при спавне жука за краем поля

    fun speedMultiplier(gameSpeed: Int): Float =    //Перевод пользовательских настроек в множитель скорости
        gameSpeed / 2f
}