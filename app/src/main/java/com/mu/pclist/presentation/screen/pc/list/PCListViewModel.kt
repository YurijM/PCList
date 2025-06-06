package com.mu.pclist.presentation.screen.pc.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.model.PCModel
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_INVENTORY_NUMBER
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.FOUND_NOTHING
import com.mu.pclist.presentation.util.PC_LIST_IS_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PCListViewModel @Inject constructor(
    private val pcRepository: PCRepository
) : ViewModel() {
    var computers by mutableStateOf(emptyList<PCModel>())
    var foundComputers by mutableStateOf(emptyList<PCModel>())
    var sortBy by mutableStateOf(BY_INVENTORY_NUMBER)
    var search by mutableStateOf("")
    var searchResult = PC_LIST_IS_EMPTY

    init {
        viewModelScope.launch {
            pcRepository.pcList().collect { list ->
                computers = list.sortedBy { it.inventoryNumber }
                foundComputers = computers
            }
        }
    }

    fun onEvent(event: PCListEvent) {
        when (event) {
            is PCListEvent.OnPCListSortByChange -> {
                search = ""
                foundComputers = computers

                sortBy = event.sortBy
                foundComputers = when (sortBy) {
                    BY_FAMILY -> {
                        foundComputers.sortedWith(
                            compareBy<PCModel> { it.family }
                                .thenBy { it.name }
                                .thenBy { it.patronymic }
                        )
                    }

                    BY_OFFICES -> {
                        foundComputers.sortedWith(
                            compareBy<PCModel> { it.office }
                                .thenBy { it.inventoryNumber }
                        )
                    }

                    else -> {
                        foundComputers.sortedBy { it.inventoryNumber }
                    }
                }
            }

            is PCListEvent.OnPCListSearchChange -> {
                search = event.search
                if (search.isBlank()) {
                    foundComputers = computers
                    searchResult = PC_LIST_IS_EMPTY
                } else {
                    searchResult = FOUND_NOTHING
                    foundComputers = when (sortBy) {
                        BY_FAMILY -> {
                            computers.filter {
                                it.family.contains(search, ignoreCase = true)
                                        || it.name.contains(search, ignoreCase = true)
                                        || it.patronymic.contains(search, ignoreCase = true)
                            }
                        }

                        BY_OFFICES -> {
                            computers.filter { it.office.contains(search, ignoreCase = true) }
                        }

                        else -> {
                            computers.filter { it.inventoryNumber.contains(search, ignoreCase = true) }
                        }
                    }
                }
            }

            is PCListEvent.OnPCListDelete -> {
                viewModelScope.launch {
                    pcRepository.deletePC(
                        PCEntity(
                            id = event.pc.id,
                            inventoryNumber = event.pc.inventoryNumber,
                        )
                    )
                }
            }
        }
    }
}