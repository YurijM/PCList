package com.mu.pclist.presentation.screen.office

sealed class OfficeEvent {
    data class OnOfficeCodeChange(val code: String) : OfficeEvent()
    data class OnOfficeShortNameChange(val shortName: String) : OfficeEvent()
    data class OnOfficeOfficeChange(val office: String) : OfficeEvent()
    data class OnOfficeChiefChange(val chief: String) : OfficeEvent()
    data object OnOfficeSave : OfficeEvent()
}
