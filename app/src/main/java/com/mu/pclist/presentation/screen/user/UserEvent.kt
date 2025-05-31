package com.mu.pclist.presentation.screen.user

sealed class UserEvent {
    data class OnUserServiceNumberChange(val serviceNumber: String) : UserEvent()
    data class OnUserFamilyChange(val family: String) : UserEvent()
    data class OnUserNameChange(val name: String) : UserEvent()
    data class OnUserPatronymicChange(val patronymic: String) : UserEvent()
    data class OnUserOfficeChange(val office: String) : UserEvent()
    data class OnUserPCChange(val pc: String) : UserEvent()
    data object OnUserSave : UserEvent()
}