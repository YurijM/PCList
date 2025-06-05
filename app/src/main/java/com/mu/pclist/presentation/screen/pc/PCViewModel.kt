package com.mu.pclist.presentation.screen.pc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.model.UserModel
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.domain.repository.UserRepository
import com.mu.pclist.presentation.navigation.Destinations.PCDestination
import com.mu.pclist.presentation.util.NEW_ID
import com.mu.pclist.presentation.util.checkIsFieldEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PCViewModel @Inject constructor(
    private val pcRepository: PCRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var id by mutableLongStateOf(NEW_ID)
    private var newPC = true
    var pc by mutableStateOf(PCEntity())
    private var users = emptyList<UserModel>()
    var familyList = mutableStateListOf<String>()
    var owner by mutableStateOf(UserModel())

    var inventoryNumberError = ""
        private set

    init {
        val args = savedStateHandle.toRoute<PCDestination>()
        id = args.id

        if (id != NEW_ID) {
            newPC = false

            viewModelScope.launch {
                pcRepository.pc(id).collect { item ->
                    pc = item

                    familyList.add("")
                    userRepository.userList().collect { list ->
                        users = list.sortedWith(
                            compareByDescending<UserModel> { it.family }
                                .thenBy { it.name }
                                .thenBy { it.patronymic }
                        )
                        users.forEach { user ->
                            familyList.add(
                                "${user.family} ${user.name} ${user.patronymic}"
                            )
                        }
                        owner = users.find { it.id.toInt() == pc.userId} ?: UserModel()
                    }
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

            is PCEvent.OnPCUserChange -> {
                val id: Int?
                if (event.user.isEmpty()) {
                    id = null
                    owner = UserModel()
                } else {
                    owner = users.find { "${it.family} ${it.name} ${it.patronymic}" == event.user } ?: UserModel()
                    id = owner.id.toInt()
                }
                pc = pc.copy(userId = id)
            }

            is PCEvent.OnPCSave -> {
                if (pc.userId == 0)
                    pc = pc.copy(userId = null)

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