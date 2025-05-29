package com.mu.pclist.presentation.navigation.destination.office

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.OfficeListDestination

fun NavController.navigationToOfficeList() {
    navigate(OfficeListDestination) {
        popUpTo(OfficeListDestination)
    }
}