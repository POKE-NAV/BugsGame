package com.example.bugsgame.ui.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegistrationRoute(
    modifier: Modifier = Modifier,
    viewModel: RegistrationViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    RegistrationScreen(
        state = state,
        onFioChanged = viewModel::onFioChanged,
        onGenderChanged = viewModel::onGenderChanged,
        onCourseChanged = viewModel::onCourseChanged,
        onDifficultyChanged = viewModel::onDifficultyChanged,
        onBirthDateChanged = viewModel::onBirthDateChanged,
        modifier = modifier,
    )
}
