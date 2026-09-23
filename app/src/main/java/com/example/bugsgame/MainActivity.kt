package com.example.bugsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.example.bugsgame.ui.theme.BugsGameTheme

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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Greeting(modifier: Modifier = Modifier) {
    var text by rememberSaveable { mutableStateOf("") }

    val genders = listOf<String>("Мужской", "Женский")
    var selectGenders by rememberSaveable {mutableStateOf<String>(genders.first())}

    val courses = listOf<String>("1 курс", "2 курс", "3 курс", "4 курс")
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedCourses by rememberSaveable { mutableStateOf(courses.first()) }

    Column(modifier = Modifier) {
        Text("Bugs game")
        TextField(
            value = text,
            onValueChange = { text = it },
            label = {Text("Фиo")},
            textStyle = TextStyle(fontFamily = FontFamily.Default)
        )
        Row() {
            Column() {
                Text(genders.first())
                Text(genders.last())
            }
            Column() {
                genders.forEach { gender ->
                    RadioButton(
                        selected = gender == selectGenders,
                        onClick = {selectGenders = gender})
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it}
        ) {
            OutlinedTextField(
                value = selectedCourses,
                onValueChange = {},
                readOnly = true,
                label = { Text("Выберите курс")},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false}
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    BugsGameTheme {
        Greeting()
    }
}