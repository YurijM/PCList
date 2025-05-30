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

    init {
        val args = savedStateHandle.toRoute<OfficeDestination>()
        id = args.id

        if (id != NEW_ID) {
            newOffice = false

            viewModelScope.launch {
                officeRepository.office(id).collect { item ->
                    office = OfficeEntity(
                        id = item.id,
                        code = item.code,
                        office = item.office,
                        shortName = item.shortName,
                        userId = item.userId
                    )
                }
            }
            viewModelScope.launch {
                familyList.add("")
                userRepository.userList().collect { list ->
                    users = list.filter { it.officeId == id }
                        .sortedWith(
                            compareByDescending<UserModel> { it.family }
                                .thenBy { it.name }
                                .thenBy { it.patronymic }
                        )
                    users.forEach { user ->
                        if (user.id.toInt() == office.userId)
                            chief = user
                        familyList.add(
                            "${user.family} ${user.name} ${user.patronymic}"
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: OfficeEvent) {
        when (event) {
            is OfficeEvent.OnOfficeCodeChange -> {
                office = office.copy(code = event.code)
            }

            is OfficeEvent.OnOfficeShortNameChange -> {
                office = office.copy(shortName = event.shortName)
            }

            is OfficeEvent.OnOfficeOfficeChange -> {
                office = office.copy(office = event.office)
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
}
