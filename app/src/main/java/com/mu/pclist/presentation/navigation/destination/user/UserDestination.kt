package com.mu.pclist.presentation.navigation.destination.user

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations
import com.mu.pclist.presentation.navigation.Destinations.UserDestination
import com.mu.pclist.presentation.screen.user.UserScreen

fun NavGraphBuilder.user(
    toUserList: (Destinations.UserListDestination) -> Unit
) {
    composable<UserDestination> {
        UserScreen(
            toUserList = toUserList
        )
    }
}