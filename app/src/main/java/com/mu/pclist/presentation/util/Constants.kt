package com.mu.pclist.presentation.util

import com.mu.pclist.R
import com.mu.pclist.presentation.navigation.Destinations

const val DEBUG_TAG = "pclist"
const val YEAR_START = 2025
const val NEW_ID = 0L

const val BY_FAMILY = "фамилии"
const val BY_SERVICE_NUMBER = "таб.№"
const val BY_OFFICES = "отделам"

const val FIELD_CAN_NOT_BE_EMPTY = "Поле не может быть пустым"

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
        titleId = R.string.computers,
        iconId = R.drawable.ic_pc,
        destination = Destinations.PCListDestination
    )
}
