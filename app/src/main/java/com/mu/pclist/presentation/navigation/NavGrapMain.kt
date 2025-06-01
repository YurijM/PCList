package com.mu.pclist.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mu.pclist.presentation.navigation.destination.office.navigateToOffice
import com.mu.pclist.presentation.navigation.destination.office.navigationToOfficeList
import com.mu.pclist.presentation.navigation.destination.office.office
import com.mu.pclist.presentation.navigation.destination.office.officeList
import com.mu.pclist.presentation.navigation.destination.pc.navigateToPC
import com.mu.pclist.presentation.navigation.destination.pc.navigationToPCList
import com.mu.pclist.presentation.navigation.destination.pc.pc
import com.mu.pclist.presentation.navigation.destination.pc.pcList
import com.mu.pclist.presentation.navigation.destination.user.navigateToUser
import com.mu.pclist.presentation.navigation.destination.user.navigationToUserList
import com.mu.pclist.presentation.navigation.destination.user.user
import com.mu.pclist.presentation.navigation.destination.user.userList

@Composable
fun NavGraphMain(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.PCListDestination
    ) {
        officeList(
            toOffice = { args ->
                navController.navigateToOffice(args)
            }
        )
        office(
            toOfficeList = { navController.navigationToOfficeList() }
        )
        userList(
            toUser = { args ->
                navController.navigateToUser(args)
            }
        )
        user(
            toUserList = { navController.navigationToUserList() }
        )
        pcList(
            toPC = { args ->
                navController.navigateToPC(args)
            }
        )
        pc(
            toPCList = { navController.navigationToPCList() }
        )
    }
}