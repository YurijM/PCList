package com.mu.pclist.presentation.navigation.destination.office

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.OfficeDestination
import com.mu.pclist.presentation.navigation.Destinations.OfficeListDestination
import com.mu.pclist.presentation.screen.office.list.OfficeListScreen

fun NavGraphBuilder.officeList(
    toOffice: (OfficeDestination) -> Unit
) {
    composable<OfficeListDestination> {
        OfficeListScreen(
            toOffice = toOffice
        )
    }
}