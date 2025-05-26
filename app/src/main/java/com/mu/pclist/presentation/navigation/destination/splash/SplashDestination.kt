package com.mu.pclist.presentation.navigation.destination.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mu.pclist.presentation.navigation.Destinations.SplashDestination
import com.mu.pclist.presentation.screen.splash.SplashScreen

fun NavGraphBuilder.splash(
    toMain: () -> Unit
) {
    composable<SplashDestination> {
        SplashScreen(
            toMain = toMain
        )
    }
}