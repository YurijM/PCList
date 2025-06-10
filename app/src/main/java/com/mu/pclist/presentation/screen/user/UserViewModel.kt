package com.mu.pclist.presentation.screen.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.model.OfficeModel
import com.mu.pclist.domain.model.PCModel
import com.mu.pclist.domain.repository.OfficeRepository
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.domain.repository.UserRepository
import com.mu.pclist.presentation.navigation.Destinations.UserDestination
import com.mu.pclist.presentation.util.NEW_ID
import com.mu.pclist.presentation.util.checkIsFieldEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val pcRepository: PCRepository,
    private val officeRepository: OfficeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var id by mutableLongStateOf(NEW_ID)
    private var newUser = true
    var user by mutableStateOf(UserEntity())
    private var offices = emptyList<OfficeModel>()
    private var computers = emptyList<PCModel>()
    var officeList = mutableStateListOf<String>()
        private set
    var office by mutableStateOf(OfficeModel())
        private set
    var pcList = mutableStateListOf<String>()
        private set
    var pc by mutableStateOf(PCModel())
        private set

    var familyError = ""
        private set
    var nameError = ""
        private set
    var patronymicError = ""
        private set
    var serviceNumberError = ""
        private set
    var phoneError = ""
        private set
    var enabled = false
        private set

    init {
        val args = savedStateHandle.toRoute<UserDestination>()
        id = args.id

        viewModelScope.launch {
            officeList.add("")
            officeRepository.officeList().collect { list ->
                offices = list.sortedBy { it.shortName }
                offices.forEach { item ->
                    officeList.add(item.shortName)
                }

                pcList.add("")
                pcRepository.pcList().collect { list ->
                    computers = list.sortedBy { it.inventoryNumber }
                    computers.forEach { item ->
                        pcList.add(item.inventoryNumber)
                    }

                    if (id != NEW_ID) {
                        newUser = false
                        userRepository.user(id).collect { item ->
                            user = item
                            enabled = checkValue()

                            office = offices.find { it.id.toInt() == user.officeId } ?: OfficeModel()
                            pc = computers.find { it.id.toInt() == user.pcId } ?: PCModel()
                        }
                    } else {
                        enabled = checkValue()
                    }
                }
            }
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.OnUserFamilyChange -> {
                user = user.copy(family = event.family)
                familyError = checkIsFieldEmpty(event.family.trim())
                enabled = checkValue()
            }

            is UserEvent.OnUserNameChange -> {
                user = user.copy(name = event.name)
                nameError = checkIsFieldEmpty(event.name.trim())
                enabled = checkValue()
            }

            is UserEvent.OnUserPatronymicChange -> {
                user = user.copy(patronymic = event.patronymic)
                patronymicError = checkIsFieldEmpty(event.patronymic.trim())
                enabled = checkValue()
            }

            is UserEvent.OnUserServiceNumberChange -> {
                user = user.copy(serviceNumber = event.serviceNumber)
                serviceNumberError = checkIsFieldEmpty(event.serviceNumber.trim())
                enabled = checkValue()
            }

            is UserEvent.OnUserPhoneChange -> {
                user = user.copy(phone = event.phone)
                phoneError = checkIsFieldEmpty(event.phone.trim())
                enabled = checkValue()
            }

            is UserEvent.OnUserOfficeChange -> {
                val id: Int?
                if (event.office.isEmpty()) {
                    id = null
                    office = OfficeModel()
                } else {
                    office = offices.find { it.shortName == event.office } ?: OfficeModel()
                    id = office.id.toInt()
                }
                user = user.copy(officeId = id)
            }

            is UserEvent.OnUserPCChange -> {
                val id: Int?
                if (event.pc.isEmpty()) {
                    id = null
                    pc = PCModel()
                } else {
                    pc = computers.find { it.inventoryNumber == event.pc } ?: PCModel()
                    id = pc.id.toInt()
                }
                user = user.copy(pcId = id)
            }

            is UserEvent.OnUserSave -> {
                if (user.officeId == 0)
                    user = user.copy(officeId = null)

                user = user.copy(
                    family = user.family.trim(),
                    name = user.name.trim(),
                    patronymic = user.patronymic.trim(),
                    serviceNumber = user.serviceNumber.trim(),
                    phone = user.phone.trim(),
                )

                if (user.pcId == 0)
                    user = user.copy(pcId = null)

                viewModelScope.launch {
                    if (newUser)
                        userRepository.insert(user)
                    else
                        userRepository.update(user)
                }
            }
        }
    }

    private fun checkValue(): Boolean =
        user.family.isNotBlank()
                && user.name.isNotBlank()
                && user.patronymic.isNotBlank()
                && user.serviceNumber.isNotBlank()
                && user.phone.isNotBlank()
}