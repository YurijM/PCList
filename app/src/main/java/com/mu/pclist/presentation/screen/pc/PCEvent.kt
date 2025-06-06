package com.mu.pclist.presentation.screen.pc

sealed class PCEvent {
    data class OnPCInventoryNumberChange(val inventoryNumber: String) : PCEvent()
    data object OnPCSave : PCEvent()
}