package com.mu.pclist.presentation.navigation.destination.office

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.OfficeDestination
import com.mu.pclist.presentation.screen.office.OfficeScreen

fun NavGraphBuilder.office(
    toOfficeList: () -> Unit
) {
    composable<OfficeDestination> {
        OfficeScreen(
            toOfficeList = toOfficeList
        )
    }
}