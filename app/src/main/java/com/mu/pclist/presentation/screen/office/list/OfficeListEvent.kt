package com.mu.pclist.presentation.screen.office.list

import android.content.Context
import com.mu.pclist.domain.model.OfficeModel

sealed class OfficeListEvent {
    data class OnOfficeListDelete(val office: OfficeModel) : OfficeListEvent()
    data class OnOfficeListDocCreate(val context: Context) : OfficeListEvent()
}