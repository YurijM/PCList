package com.mu.pclist.presentation.navigation.destination.office

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.OfficeListDestination
import com.mu.pclist.presentation.navigation.Destinations.OfficeDestination

fun NavController.navigateToOffice(args: OfficeDestination) {
    navigate(OfficeDestination(args.id)) {
        popUpTo(OfficeListDestination)
    }
}