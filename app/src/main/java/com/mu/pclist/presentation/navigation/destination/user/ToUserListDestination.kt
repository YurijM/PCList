package com.mu.pclist.presentation.navigation.destination.user

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.UserListDestination

fun NavController.navigationToUserList() {
    navigate(UserListDestination) {
        popUpTo(UserListDestination)
    }
}