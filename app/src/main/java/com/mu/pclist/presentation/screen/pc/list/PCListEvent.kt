package com.mu.pclist.presentation.screen.pc.list

import com.mu.pclist.data.entity.PCEntity

sealed class PCListEvent {
    data class OnEventPCListDelete(val pc: PCEntity) : PCListEvent()
}