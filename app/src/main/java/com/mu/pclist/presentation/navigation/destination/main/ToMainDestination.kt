package com.mu.pclist.presentation.navigation.destination.main

import androidx.navigation.NavController
import com.mu.pclist.presentation.navigation.Destinations.MainDestination
import com.mu.pclist.presentation.navigation.Destinations.SplashDestination

fun NavController.navigationToMain() {
    navigate(MainDestination) {
        popUpTo(SplashDestination) {
            //inclusive = true
        }
    }
}