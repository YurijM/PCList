package com.mu.pclist.presentation.navigation.destination

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mu.pclist.presentation.navigation.Destinations.SplashDestination
import com.mu.pclist.presentation.navigation.destination.main.main
import com.mu.pclist.presentation.navigation.destination.main.navigationToMain
import com.mu.pclist.presentation.navigation.destination.splash.splash

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = SplashDestination
    ) {
        splash(
            toMain = { navController.navigationToMain() }
        )
        main()
    }
}