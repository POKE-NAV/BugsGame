package com.example.bugsgame.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(),
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    SettingsScreen(
        settings = settings,
        onGameSpeedChanged = viewModel::onGameSpeedChanged,
        onMaxBugsChanged = viewModel::onMaxBugsChanged,
        onBonusIntervalChanged = viewModel::onBonusIntervalChanged,
        onRoundDurationChanged = viewModel::onRoundDurationChanged,
        onReset = viewModel::onReset,
        modifier = modifier,
    )
}
