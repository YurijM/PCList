package com.mu.pclist.presentation.screen.pc

import androidx.lifecycle.ViewModel
import com.mu.pclist.domain.repository.PCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PCViewModel @Inject constructor(
    private val pcRepository: PCRepository
) : ViewModel() {
    init {

    }

    fun onEvent(event: PCEvent) {
        when (event) {
            is PCEvent.OnPCEventInventoryNumberChange -> TODO()
            is PCEvent.OnPCEventUserChange -> TODO()
            is PCEvent.OnPCEventSave -> TODO()
        }
    }
}