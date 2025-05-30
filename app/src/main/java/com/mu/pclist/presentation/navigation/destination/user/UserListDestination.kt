package com.mu.pclist.presentation.navigation.destination.user

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.UserListDestination
import com.mu.pclist.presentation.screen.user.list.UserListScreen

fun NavGraphBuilder.userList() {
    composable<UserListDestination> {
        UserListScreen()
    }
}