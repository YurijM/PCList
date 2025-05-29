package com.mu.pclist.presentation.navigation.destination.office

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.OfficeListDestination
import com.mu.pclist.presentation.screen.office.list.OfficeListScreen

fun NavGraphBuilder.officeList() {
    composable<OfficeListDestination> {
        OfficeListScreen()
    }
}