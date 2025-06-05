package com.mu.pclist.presentation.screen.pc.list

import com.mu.pclist.domain.model.PCModel

sealed class PCListEvent {
    data class OnPCListSortByChange(val sortBy: String) : PCListEvent()
    data class OnPCListSearchChange(val search: String) : PCListEvent()
    data class OnPCListDelete(val pc: PCModel) : PCListEvent()
}