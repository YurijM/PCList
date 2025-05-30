package com.mu.pclist.presentation.screen.office.list

import com.mu.pclist.domain.model.OfficeModel

sealed class OfficeListEvent {
    data class OnOfficeListDelete(val office: OfficeModel) : OfficeListEvent()
}