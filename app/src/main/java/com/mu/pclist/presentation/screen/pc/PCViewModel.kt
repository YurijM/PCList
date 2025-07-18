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
import com.mu.pclist.presentation.util.BY_INVENTORY_NUMBER
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
    var user by mutableStateOf(UserModel())

    var saved by mutableStateOf(false)
    var sortedBy = BY_INVENTORY_NUMBER

    var inventoryNumberError = ""
        private set

    init {
        val args = savedStateHandle.toRoute<PCDestination>()
        id = args.id
        sortedBy = args.sortedBy

        viewModelScope.launch {
            familyList.add("")
            userRepository.userList().collect { list ->
                users = list.sortedWith(
                        compareBy<UserModel> { it.family }
                            .thenBy { it.name }
                            .thenBy { it.patronymic }
                    )
                users.forEach { user ->
                    familyList.add(
                        "${user.family} ${user.name} ${user.patronymic}"
                    )
                }

                if (id != NEW_ID) {
                    newPC = false
                    pcRepository.pc(id).collect { item ->
                        pc = item

                        user = users.find { it.id.toInt() == pc.userId } ?: UserModel()
                    }
                }
            }
        }
    }

    fun onEvent(event: PCEvent) {
        when (event) {
            is PCEvent.OnPCInventoryNumberChange -> {
                pc = pc.copy(inventoryNumber = event.inventoryNumber)
                inventoryNumberError = checkIsFieldEmpty(event.inventoryNumber.trim())
            }

            is PCEvent.OnPCSave -> {
                viewModelScope.launch {
                    pc = pc.copy(inventoryNumber = pc.inventoryNumber.trim())

                    if (newPC) {
                        val id = pcRepository.insert(pc)
                        pc = pc.copy(id = id)
                    }
                    else
                        pcRepository.update(pc)
                }
                saved = true
            }

            is PCEvent.OnPCUserChange -> {
                val id: Int?
                if (event.user.isEmpty()) {
                    id = null
                    user = UserModel()
                } else {
                    user = users.find { "${it.family} ${it.name} ${it.patronymic}" == event.user } ?: UserModel()
                    id = user.id.toInt()
                }
                pc = pc.copy(userId = id)
            }
        }
    }
}