package com.mu.pclist.presentation.util

import com.mu.pclist.R
import com.mu.pclist.presentation.navigation.Destinations

const val DEBUG_TAG = "pclist"
const val YEAR_START = 2025
const val NEW_ID = 0L

sealed class BottomNavItem(
    val titleId: Int,
    val iconId: Int,
    val destination: Destinations
) {
    data object OfficeListItem : BottomNavItem(
        titleId = R.string.offices,
        iconId = R.drawable.ic_office,
        destination = Destinations.OfficeListDestination
    )

    data object UserListItem : BottomNavItem(
        titleId = R.string.users,
        iconId = R.drawable.ic_user,
        destination = Destinations.UserListDestination
    )

    data object PCListItem : BottomNavItem(
        titleId = R.string.pc,
        iconId = R.drawable.ic_pc,
        destination = Destinations.PCListDestination
    )
}

