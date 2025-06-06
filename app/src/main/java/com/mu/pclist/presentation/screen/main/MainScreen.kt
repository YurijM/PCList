package com.mu.pclist.presentation.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mu.pclist.presentation.component.ApplicationBar
import com.mu.pclist.presentation.component.BottomNav
import com.mu.pclist.presentation.navigation.NavGraphMain
import com.mu.pclist.presentation.util.YEAR_START

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            BottomNav(
                currentDestination,
                YEAR_START
            ) { route ->
                navController.navigate(route)
            }
        },
        topBar = {
            ApplicationBar()
        }
    ) { paddingValues ->
        Surface(
            contentColor = MaterialTheme.colorScheme.primary,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
        ) {
            NavGraphMain(navController = navController)

            /*if (isLoading) {
                    AppProgressBar()
                }*/
        }
    }
}