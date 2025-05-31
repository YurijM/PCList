package com.mu.pclist.presentation.navigation.destination.user

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.UserDestination
import com.mu.pclist.presentation.navigation.Destinations.UserListDestination

fun NavController.navigateToUser(args: UserDestination) {
    navigate(UserDestination(args.id)) {
        popUpTo(UserListDestination)
    }
}