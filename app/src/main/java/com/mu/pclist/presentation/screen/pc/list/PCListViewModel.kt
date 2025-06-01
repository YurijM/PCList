package com.mu.pclist.presentation.screen.pc.list

import androidx.lifecycle.ViewModel
import com.mu.pclist.domain.repository.PCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PCListViewModel @Inject constructor(
    private val pcRepository: PCRepository
) : ViewModel() {
    fun onEvent(event: PCListEvent) {
        when (event) {
            is PCListEvent.OnEventPCListDelete -> {

            }
        }
    }
}