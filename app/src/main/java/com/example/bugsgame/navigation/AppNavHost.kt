package com.example.bugsgame.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.bugsgame.ui.registration.RegistrationRoute
import com.example.bugsgame.ui.authors.AuthorsScreen
import com.example.bugsgame.ui.rules.RulesScreen
import com.example.bugsgame.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost() {
    val pagerState = rememberPagerState(pageCount = { navItems.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding),
        ) { page ->
            when (page) {
                0 -> RegistrationRoute()
                1 -> RulesScreen()
                2 -> AuthorsScreen()
                3 -> SettingsScreen()
            }
        }
    }
}

// @Composable
// fun AppNavHost() {
//     val navController = rememberNavController()

//     val backStackEntry by navController.currentBackStackEntryAsState()
//     val currentRoute = backStackEntry?.destination?.route

//     Scaffold(
//         bottomBar = {
//             NavigationBar {
//                 navItems.forEach { item ->
//                     NavigationBarItem(
//                         selected = currentRoute == item.route,
//                         onClick = {
//                             navController.navigate(item.route) {
//                                 popUpTo(navController.graph.findStartDestination().id) {
//                                     saveState = true
//                                 }
//                                 launchSingleTop = true
//                                 restoreState = true
//                             }
//                         },
//                         icon = { Icon(item.icon, contentDescription = item.title) },
//                         label = { Text(item.title) },
//                     )
//                 }
//             }
//         }
//     ) { innerPadding ->
//         NavHost(
//             navController = navController,
//             startDestination = Routes.REGISTRATION,
//             modifier = Modifier.padding(innerPadding),
//         ) {
//             composable(Routes.REGISTRATION) { Greeting() }
//             composable(Routes.RULES) { RulesScreen() }
//             composable(Routes.AUTHORS) { AuthorsScreen() }
//             composable(Routes.SETTINGS) { SettingsScreen() }
//         }
//     }
// }
