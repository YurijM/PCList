package com.mu.pclist.presentation.screen.pc.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.repository.PCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PCListViewModel @Inject constructor(
    private val pcRepository: PCRepository
) : ViewModel() {
    var pcList = pcRepository.pcList()

    fun onEvent(event: PCListEvent) {
        when (event) {
            is PCListEvent.OnPCListDelete -> {
                viewModelScope.launch {
                    pcRepository.delete(
                        PCEntity(
                            id = event.pc.id,
                            inventoryNumber = event.pc.inventoryNumber,
                            userId = event.pc.userId.toInt()
                        )
                    )
                }
            }
        }
    }
}