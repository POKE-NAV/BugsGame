package com.example.bugsgame.ui.registration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
object RegistrationOptions {
    val genders = listOf("Мужской", "Женский")
    val courses = listOf("1 курс", "2 курс", "3 курс", "4 курс")
    val difficulties = listOf("Легкий", "Средний", "Сложный")
}
data class RegistrationUiState(
    val fio: String = "",
    val gender: String = RegistrationOptions.genders.first(),
    val course: String = RegistrationOptions.courses.first(),
    val difficultyIndex: Int = 0,
    val birthDate: LocalDate? = null,
)
class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onFioChanged(value: String) = _uiState.update { it.copy(fio = value) }

    fun onGenderChanged(value: String) = _uiState.update { it.copy(gender = value) }

    fun onCourseChanged(value: String) = _uiState.update { it.copy(course = value) }

    fun onDifficultyChanged(value: Int) = _uiState.update { it.copy(difficultyIndex = value) }

    fun onBirthDateChanged(value: LocalDate?) = _uiState.update { it.copy(birthDate = value) }
}
