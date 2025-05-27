package com.mu.pclist.presentation.navigation.destination.pc

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.PCListDestination
import com.mu.pclist.presentation.screen.pc.PCListScreen

fun NavGraphBuilder.pcList() {
    composable<PCListDestination> {
        PCListScreen()
    }
}