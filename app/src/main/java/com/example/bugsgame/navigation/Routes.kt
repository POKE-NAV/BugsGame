package com.example.bugsgame.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {
    const val REGISTRATION = "registration"
    const val RULES = "rules"
    const val AUTHORS = "authors"
    const val SETTINGS = "settings"
}

data class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

val navItems = listOf(
    NavItem(Routes.REGISTRATION, "Регистрация", Icons.Filled.Person),
    NavItem(Routes.RULES, "Правила", Icons.Filled.Info),
    NavItem(Routes.AUTHORS, "Авторы", Icons.Filled.Face),
    NavItem(Routes.SETTINGS, "Настройки", Icons.Filled.Settings),
)
