package com.mu.pclist.presentation.navigation.destination.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.MainDestination
import com.mu.pclist.presentation.screen.main.MainScreen

fun NavGraphBuilder.main() {
    composable<MainDestination> {
        MainScreen()
    }
}