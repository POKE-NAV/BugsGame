package com.example.bugsgame.model

import com.example.bugsgame.R
import java.time.LocalDate

enum class Zodiac(val displayName: String, val resId: Int) {
    OVEN("Овен", R.mipmap.oven_foreground),
    TAURUS("Телец", R.mipmap.taurus_foreground),
    TWINS("Близнецы", R.mipmap.twins_foreground),
    RAK("Рак", R.mipmap.rak_foreground),
    LION("Лев", R.mipmap.lion_foreground),
    VIRGIN("Дева", R.mipmap.virgin_foreground),
    SCALES("Весы", R.mipmap.scales_foreground),
    SCORPIO("Скорпион", R.mipmap.scorpio_foreground),
    SAGITTARIUS("Стрелец", R.mipmap.sagittarius_foreground),
    CAPRICORN("Козерог", R.mipmap.capricorn_foreground),
    AQUARIUS("Водолей", R.mipmap.aquarius_foreground),
    FISH("Рыбы", R.mipmap.fish_foreground)
}

fun zodiacFor(date: LocalDate): Zodiac {
    return when {
        date.monthValue == 1 && date.dayOfMonth <= 19 -> Zodiac.CAPRICORN
        date.monthValue == 1 -> Zodiac.AQUARIUS
        date.monthValue == 2 && date.dayOfMonth <= 18 -> Zodiac.AQUARIUS
        date.monthValue == 2 -> Zodiac.FISH
        date.monthValue == 3 && date.dayOfMonth <= 20 -> Zodiac.FISH
        date.monthValue == 3 -> Zodiac.OVEN
        date.monthValue == 4 && date.dayOfMonth <= 19 -> Zodiac.OVEN
        date.monthValue == 4 -> Zodiac.TAURUS
        date.monthValue == 5 && date.dayOfMonth <= 20 -> Zodiac.TAURUS
        date.monthValue == 5 -> Zodiac.TWINS
        date.monthValue == 6 && date.dayOfMonth <= 20 -> Zodiac.TWINS
        date.monthValue == 6 -> Zodiac.RAK
        date.monthValue == 7 && date.dayOfMonth <= 22 -> Zodiac.RAK
        date.monthValue == 7 -> Zodiac.LION
        date.monthValue == 8 && date.dayOfMonth <= 22 -> Zodiac.LION
        date.monthValue == 8 -> Zodiac.VIRGIN
        date.monthValue == 9 && date.dayOfMonth <= 22 -> Zodiac.VIRGIN
        date.monthValue == 9 -> Zodiac.SCALES
        date.monthValue == 10 && date.dayOfMonth <= 22 -> Zodiac.SCALES
        date.monthValue == 10 -> Zodiac.SCORPIO
        date.monthValue == 11 && date.dayOfMonth <= 21 -> Zodiac.SCORPIO
        date.monthValue == 11 -> Zodiac.SAGITTARIUS
        date.monthValue == 12 && date.dayOfMonth <= 21 -> Zodiac.SAGITTARIUS
        else -> Zodiac.CAPRICORN
    }
}