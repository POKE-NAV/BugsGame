package com.example.bugsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bugsgame.ui.theme.BugsGameTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BugsGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")


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
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Greeting(modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("") }

    val genders = listOf<String>("Мужской", "Женский")
    var selectGenders by rememberSaveable {mutableStateOf<String>(genders.first())}

    val courses = listOf<String>("1 курс", "2 курс", "3 курс", "4 курс")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCourses by rememberSaveable { mutableStateOf(courses.first()) }

    val difficulties = listOf<String>("Легкий", "Средний", "Сложный")
    var selectDifficulties by rememberSaveable { mutableStateOf(0) }

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var birthDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Bugs game")
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Фио") },
                    textStyle = TextStyle(fontFamily = FontFamily.Default)
                )
            }

            Column {
                genders.forEach { gender ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == selectGenders,
                            onClick = { selectGenders = gender }
                        )
                        Text(gender)
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCourses,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выберите курс") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    courses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course) },
                            onClick = {
                                selectedCourses = course
                                expanded = false
                            }
                        )

                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Выберите сложность!")
                Slider(
                    value = selectDifficulties.toFloat(),
                    onValueChange = { selectDifficulties = it.roundToInt() },
                    valueRange = 0f..(difficulties.size - 1).toFloat(),
                    steps = difficulties.size - 2,
                    modifier = Modifier.fillMaxWidth(),
                    thumb = {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(
                                    shape = CircleShape,
                                    color = Color.Blue
                                )
                        )
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    difficulties.forEach { difficult ->
                        Text(difficult)
                    }
                }
            }

            Box {
                OutlinedTextField(
                    value = birthDate?.format(FORMATTER) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата рождения") },
                    placeholder = { Text("dd.mm.yyyy") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(Modifier.matchParentSize().clickable { showDialog = true })

                if (showDialog) {
                    val currentYear = LocalDate.now().year

                    val datePickerState = rememberDatePickerState(
                        yearRange = 1900..currentYear,
                        selectableDates = object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                return utcTimeMillis <= System.currentTimeMillis()
                            }
                            override fun isSelectableYear(year: Int): Boolean {
                                return year <= currentYear
                            }
                        }
                    )

                    DatePickerDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    birthDate = datePickerState.selectedDateMillis?.let {
                                        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                                    }
                                    showDialog = false
                                }
                            ) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) { Text("Отмена") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            birthDate?.let { date ->
                val zodiac = zodiacFor(date)
                Image(
                    painter = painterResource(id = zodiac.resId),
                    contentDescription = zodiac.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    BugsGameTheme {
        Greeting()
    }
}