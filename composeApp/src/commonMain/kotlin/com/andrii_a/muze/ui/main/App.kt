package com.andrii_a.muze.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.andrii_a.muze.ui.navigation.MainNavigationHost
import com.andrii_a.muze.ui.navigation.NavigationScreen
import com.andrii_a.muze.ui.navigation.Screen
import com.andrii_a.muze.ui.theme.MuzeTheme
import com.andrii_a.muze.ui.util.currentScreenClassName
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.navigation_icon
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        MuzeTheme {
            val navController = rememberNavController()
            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

            val adaptiveInfo = currentWindowAdaptiveInfo()
            val navigationSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)

            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    NavigationScreen.entries.forEachIndexed { index, entry ->
                        item(
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedTabIndex)
                                        entry.iconSelected
                                    else
                                        entry.iconUnselected,
                                    contentDescription = stringResource(resource = Res.string.navigation_icon)
                                )
                            },
                            label = { Text(text = stringResource(resource = entry.titleRes)) },
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index
                                navController.navigate(entry.screen) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true

                                }
                            },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                },
                layoutType = if (navController.currentScreenClassName == Screen.ArtworkDetail::class.simpleName)
                    NavigationSuiteType.None
                else
                    navigationSuiteType
            ) {
                MainNavigationHost(navHostController = navController)
            }
        }
    }
}