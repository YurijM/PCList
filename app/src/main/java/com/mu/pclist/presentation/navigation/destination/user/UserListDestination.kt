package com.mu.pclist.presentation.navigation.destination.user

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.UserDestination
import com.mu.pclist.presentation.navigation.Destinations.UserListDestination
import com.mu.pclist.presentation.screen.user.list.UserListScreen

fun NavGraphBuilder.userList(
    toUser: (UserDestination) -> Unit
) {
    composable<UserListDestination> {
        UserListScreen(
            toUser = toUser
        )
    }
}