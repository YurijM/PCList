package com.mu.pclist.presentation.screen.office

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.model.UserModel
import com.mu.pclist.domain.repository.OfficeRepository
import com.mu.pclist.domain.repository.UserRepository
import com.mu.pclist.presentation.navigation.Destinations.OfficeDestination
import com.mu.pclist.presentation.util.NEW_ID
import com.mu.pclist.presentation.util.checkIsFieldEmpty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficeViewModel @Inject constructor(
    private val officeRepository: OfficeRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var id by mutableLongStateOf(NEW_ID)
    private var newOffice = true
    var office by mutableStateOf(OfficeEntity())
    private var users = emptyList<UserModel>()
    var familyList = mutableStateListOf<String>()
    var chief by mutableStateOf(UserModel())

    var codeError = ""
        private set
    var shortNameError = ""
        private set
    var officeError = ""
        private set
    var enabled = false
        private set

    init {
        val args = savedStateHandle.toRoute<OfficeDestination>()
        id = args.id

        viewModelScope.launch {
            familyList.add("")
            userRepository.userList().collect { list ->
                users = list.filter { it.officeId == id }
                    .sortedWith(
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
                    newOffice = false
                    officeRepository.office(id).collect { item ->
                        office = item
                        enabled = checkValue()

                        chief = users.find { it.id.toInt() == office.userId } ?: UserModel()
                    }
                } else {
                    enabled = checkValue()
                }
            }
        }
    }

    fun onEvent(event: OfficeEvent) {
        when (event) {
            is OfficeEvent.OnOfficeCodeChange -> {
                office = office.copy(code = event.code)
                codeError = checkIsFieldEmpty(event.code)
                enabled = checkValue()
            }

            is OfficeEvent.OnOfficeShortNameChange -> {
                office = office.copy(shortName = event.shortName)
                shortNameError = checkIsFieldEmpty(event.shortName)
                enabled = checkValue()
            }

            is OfficeEvent.OnOfficeOfficeChange -> {
                office = office.copy(office = event.office)
                officeError = checkIsFieldEmpty(event.office)
                enabled = checkValue()
            }

            is OfficeEvent.OnOfficeChiefChange -> {
                val id: Int?
                if (event.chief.isEmpty()) {
                    id = null
                    chief = UserModel()
                } else {
                    chief = users.find { "${it.family} ${it.name} ${it.patronymic}" == event.chief } ?: UserModel()
                    id = chief.id.toInt()
                }
                office = office.copy(userId = id)
            }

            is OfficeEvent.OnOfficeSave -> {
                if (office.userId == 0)
                    office = office.copy(userId = null)

                viewModelScope.launch {
                    if (newOffice)
                        officeRepository.insert(office)
                    else
                        officeRepository.update(office)
                }
            }
        }
    }

    private fun checkValue(): Boolean =
        office.code.isNotBlank() && office.shortName.isNotBlank() && office.office.isNotBlank()
}
