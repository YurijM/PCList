package com.mu.pclist.presentation.screen.pc

sealed class PCEvent {
    data class OnPCInventoryNumberChange(val inventoryNumber: String) : PCEvent()
    data class OnPCUserChange(val user: String) : PCEvent()
    data object OnPCSave : PCEvent()
}