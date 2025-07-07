package com.mu.pclist.presentation.navigation.destination.user

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.UserListDestination
import com.mu.pclist.presentation.util.NEW_ID

fun NavController.navigationToUserList(args: UserListDestination) {
    navigate(UserListDestination(args.id)) {
        popUpTo(UserListDestination(NEW_ID))
    }
}