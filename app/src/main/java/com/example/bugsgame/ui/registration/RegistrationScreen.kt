package com.example.bugsgame.ui.registration

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bugsgame.model.zodiacFor
import com.example.bugsgame.ui.theme.BugsGameTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RegistrationScreen(
    state: RegistrationUiState,
    onFioChanged: (String) -> Unit,
    onGenderChanged: (String) -> Unit,
    onCourseChanged: (String) -> Unit,
    onDifficultyChanged: (Int) -> Unit,
    onBirthDateChanged: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showSummary by rememberSaveable { mutableStateOf(false) }

    if (showSummary) {
        RegistrationSummary(state = state, onBack = { showSummary = false })
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
                    value = state.fio,
                    onValueChange = onFioChanged,
                    label = { Text("Фио") },
                    textStyle = TextStyle(fontFamily = FontFamily.Default)
                )
            }

            Column {
                RegistrationOptions.genders.forEach { gender ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == state.gender,
                            onClick = { onGenderChanged(gender) }
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
                    value = state.course,
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
                    RegistrationOptions.courses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course) },
                            onClick = {
                                onCourseChanged(course)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Выберите сложность!")
                Slider(
                    value = state.difficultyIndex.toFloat(),
                    onValueChange = { onDifficultyChanged(it.roundToInt()) },
                    valueRange = 0f..(RegistrationOptions.difficulties.size - 1).toFloat(),
                    steps = RegistrationOptions.difficulties.size - 2,
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
                    RegistrationOptions.difficulties.forEach { difficult ->
                        Text(difficult)
                    }
                }
            }

            Box {
                OutlinedTextField(
                    value = state.birthDate?.format(FORMATTER) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Дата рождения") },
                    placeholder = { Text("dd.mm.yyyy") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(Modifier.matchParentSize().clickable { showDatePicker = true })

                if (showDatePicker) {
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
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    onBirthDateChanged(
                                        datePickerState.selectedDateMillis?.let {
                                            Instant.ofEpochMilli(it)
                                                .atZone(ZoneOffset.UTC)
                                                .toLocalDate()
                                        }
                                    )
                                    showDatePicker = false
                                }
                            ) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
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
            state.birthDate?.let { date ->
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
            Button(onClick = { showSummary = true }) {
                Text("Регистрация")
            }
        }
    }
}

@Composable
private fun RegistrationSummary(
    state: RegistrationUiState,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Регистрация")
        state.birthDate?.let { date ->
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
        Text("Фио: ${state.fio}")
        Text("Пол: ${state.gender}")
        Text("Курс: ${state.course}")
        Text("Сложность: ${RegistrationOptions.difficulties[state.difficultyIndex]}")
        Text("Дата рождения: ${state.birthDate?.format(FORMATTER) ?: ""}")
        Button(onClick = onBack) { Text("Назад") }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegistrationScreenPreview() {
    BugsGameTheme {
        RegistrationScreen(
            state = RegistrationUiState(fio = "Иван Иванов"),
            onFioChanged = {},
            onGenderChanged = {},
            onCourseChanged = {},
            onDifficultyChanged = {},
            onBirthDateChanged = {},
        )
    }
}
