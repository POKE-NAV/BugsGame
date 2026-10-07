package com.example.bugsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.bugsgame.navigation.AppNavHost
import com.example.bugsgame.ui.theme.BugsGameTheme

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
