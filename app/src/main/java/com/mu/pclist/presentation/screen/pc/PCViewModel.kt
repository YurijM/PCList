package com.mu.pclist.presentation.screen.pc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.presentation.navigation.Destinations.PCDestination
import com.mu.pclist.presentation.util.NEW_ID
import com.mu.pclist.presentation.util.checkIsFieldEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PCViewModel @Inject constructor(
    private val pcRepository: PCRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var id by mutableLongStateOf(NEW_ID)
    private var newPC = true
    var pc by mutableStateOf(PCEntity())

    var inventoryNumberError = ""
        private set

    init {
        val args = savedStateHandle.toRoute<PCDestination>()
        id = args.id

        viewModelScope.launch {
            if (id != NEW_ID) {
                newPC = false
                pcRepository.pc(id).collect { item ->
                    pc = item
                }
            }
        }
    }

    fun onEvent(event: PCEvent) {
        when (event) {
            is PCEvent.OnPCInventoryNumberChange -> {
                pc = pc.copy(inventoryNumber = event.inventoryNumber)
                inventoryNumberError = checkIsFieldEmpty(event.inventoryNumber)
            }

            is PCEvent.OnPCSave -> {
                viewModelScope.launch {
                    if (newPC)
                        pcRepository.insert(pc)
                    else
                        pcRepository.update(pc)
                }
            }
        }
    }
}