package com.mu.pclist.presentation.navigation.destination.pc

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.PCDestination
import com.mu.pclist.presentation.navigation.Destinations.PCListDestination

fun NavController.navigateToPC(args: PCDestination) {
    navigate(PCDestination(args.id)) {
        popUpTo(PCListDestination)
    }
}