package com.mu.pclist.presentation.screen.pc

sealed class PCEvent {
    data class OnPCEventInventoryNumberChange(val inventoryNumber: String) : PCEvent()
    data class OnPCEventUserChange(val user: String) : PCEvent()
    data object OnPCEventSave : PCEvent()
}