package com.mu.pclist.presentation.navigation.destination.pc

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.PCListDestination

fun NavController.navigationToPCList(args: PCListDestination) {
    navigate(PCListDestination(args.id)) {
        //popUpTo(PCListDestination)
    }
}