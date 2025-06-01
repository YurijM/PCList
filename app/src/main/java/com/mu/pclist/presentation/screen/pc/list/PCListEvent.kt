package com.mu.pclist.presentation.screen.pc.list

import com.mu.pclist.domain.model.PCModel

sealed class PCListEvent {
    data class OnPCListDelete(val pc: PCModel) : PCListEvent()
}