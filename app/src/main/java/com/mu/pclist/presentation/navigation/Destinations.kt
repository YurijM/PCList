package com.mu.pclist.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    data object SplashDestination : Destinations()
    @Serializable
    data object MainDestination : Destinations()

    @Serializable
    data object OfficeListDestination : Destinations()
    @Serializable
    data class OfficeDestination(val id: Long) : Destinations()

    @Serializable
    data object UserListDestination : Destinations()
    @Serializable
    data class UserDestination(val id: Long) : Destinations()

    @Serializable
    data object PCListDestination : Destinations()
    @Serializable
    data class PCDestination(val id: Long) : Destinations()
}