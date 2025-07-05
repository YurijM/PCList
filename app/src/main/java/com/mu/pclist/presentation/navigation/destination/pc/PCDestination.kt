package com.mu.pclist.presentation.navigation.destination.pc

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.PCListDestination
import com.mu.pclist.presentation.navigation.Destinations.PCDestination
import com.mu.pclist.presentation.screen.pc.PCScreen

fun NavGraphBuilder.pc(
    toPCList: (PCListDestination) -> Unit
) {
    composable<PCDestination> {
        PCScreen(
            toPCList = toPCList
        )
    }
}