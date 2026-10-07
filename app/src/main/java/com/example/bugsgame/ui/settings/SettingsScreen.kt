package com.example.bugsgame.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bugsgame.model.GameSettings
import com.example.bugsgame.ui.theme.BugsGameTheme
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    var settings by remember { mutableStateOf(GameSettings()) }
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingSlider(
            title = "Скорость игры",
            value = settings.gameSpeed,
            range = 1f..3f,
            steps = 1,
            onValueChange = { settings = settings.copy(gameSpeed = it) },
        )
        SettingSlider(
            title = "Макс. тараканов на экране",
            value = settings.maxBugs,
            range = 1f..20f,
            steps = 18,
            onValueChange = { settings = settings.copy(maxBugs = it) },
        )
        SettingSlider(
            title = "Интервал появления бонусов, с",
            value = settings.bonusIntervalSec,
            range = 5f..30f,
            steps = 24,
            onValueChange = { settings = settings.copy(bonusIntervalSec = it) },
        )
        SettingSlider(
            title = "Длительность раунда, с",
            value = settings.roundDurationSec,
            range = 30f..120f,
            steps = 89,
            onValueChange = { settings = settings.copy(roundDurationSec = it) },
        )

        Button(
            onClick = { showResetDialog = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Сбросить настройки")
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Сбросить настройки?") },
            text = { Text("Все параметры вернутся к значениям по умолчанию.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        settings = GameSettings()
                        showResetDialog = false
                    }
                ) {
                    Text("Сбросить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Отмена")
                }
            },
        )
    }
}

@Composable
private fun SettingSlider(
    title: String,
    value: Int,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(title)
            Text(value.toString())
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            valueRange = range,
            steps = steps,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingsScreenPreview() {
    BugsGameTheme {
        SettingsScreen()
    }
}
