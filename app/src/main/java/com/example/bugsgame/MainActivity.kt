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
import androidx.compose.material3.Button
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
import com.example.bugsgame.model.zodiacFor
import com.example.bugsgame.navigation.AppNavHost
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
                AppNavHost()
            }
        }
    }
}

private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")
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
    var showReg by rememberSaveable { mutableStateOf(false) }

    if (showReg) {
        Reg(
            text = text,
            genders = selectGenders,
            courses = selectedCourses,
            difficulties = difficulties,
            selectDifficulties = selectDifficulties,
            birthDate = birthDate,
            onBack = { showReg = false }
        )
        return
    }

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    showReg = true
                }) {
                Text("Регистрация")
            }
        }
    }
}

@Composable
fun Reg(
    text: String,
    genders: String,
    courses: String,
    difficulties: List<String>,
    selectDifficulties: Int,
    birthDate: LocalDate?,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация")
        birthDate?.let { date ->
            val zodiac = zodiacFor(date)
            Image(
                painter = painterResource(id = zodiac.resId),
                contentDescription = zodiac.displayName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Text(zodiac.displayName)
        }
        Text("Фио: $text")
        Text("Пол: $genders")
        Text("Курс: $courses")
        Text("Сложность: ${difficulties[selectDifficulties]}")
        Text("Дата рождения: ${birthDate?.format(FORMATTER) ?: ""}")
        Button(onClick = onBack) { Text("Назад") }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    BugsGameTheme {
        Greeting()
    }
}