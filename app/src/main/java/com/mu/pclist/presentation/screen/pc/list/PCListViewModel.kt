package com.mu.pclist.presentation.screen.pc.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.model.PCModel
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.presentation.navigation.Destinations.PCListDestination
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_INVENTORY_NUMBER
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.COMPUTERS
import com.mu.pclist.presentation.util.FOUND_NOTHING
import com.mu.pclist.presentation.util.PC_LIST_IS_EMPTY
import com.mu.pclist.presentation.util.setTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PCListViewModel @Inject constructor(
    private val pcRepository: PCRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var computers by mutableStateOf(emptyList<PCModel>())
    var foundComputers by mutableStateOf(emptyList<PCModel>())
    var sortBy by mutableStateOf(BY_INVENTORY_NUMBER)
    var search by mutableStateOf("")
    var searchResult = PC_LIST_IS_EMPTY
    var title = ""
        private set
    var position by mutableIntStateOf(0)

    init {
        val args = savedStateHandle.toRoute<PCListDestination>()

        viewModelScope.launch {
            pcRepository.pcList().collect { list ->
                computers = list.sortedBy { it.inventoryNumber }
                foundComputers = computers
                title = setTitle(COMPUTERS, foundComputers.size, computers.size)

                val idx = computers.indexOf(computers.find { it.id == args.id })
                if (idx > 0) {
                    position = idx
                }
            }
        }
    }

    fun onEvent(event: PCListEvent) {
        when (event) {
            is PCListEvent.OnPCListSortByChange -> {
                search = ""
                foundComputers = computers
                title = setTitle(COMPUTERS, foundComputers.size, computers.size)

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
                title = setTitle(COMPUTERS, foundComputers.size, computers.size)
            }

            is PCListEvent.OnPCListDelete -> {
                search = ""
                viewModelScope.launch {
                    pcRepository.delete(
                        PCEntity(
                            id = event.pc.id,
                            inventoryNumber = event.pc.inventoryNumber,
                        )
                    )

                    title = setTitle(COMPUTERS, foundComputers.size, computers.size)
                }
            }
        }
    }
}