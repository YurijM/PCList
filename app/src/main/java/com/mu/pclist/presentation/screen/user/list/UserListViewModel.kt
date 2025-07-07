package com.mu.pclist.presentation.screen.user.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.model.PCModel
import com.mu.pclist.domain.model.UserModel
import com.mu.pclist.domain.repository.PCRepository
import com.mu.pclist.domain.repository.UserRepository
import com.mu.pclist.presentation.navigation.Destinations.PCListDestination
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.BY_SERVICE_NUMBER
import com.mu.pclist.presentation.util.FOUND_NOTHING
import com.mu.pclist.presentation.util.USERS
import com.mu.pclist.presentation.util.USER_LIST_IS_EMPTY
import com.mu.pclist.presentation.util.setTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val pcRepository: PCRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    //var users = userRepository.userList()
    var users by mutableStateOf(emptyList<UserModel>())
    var foundUsers by mutableStateOf(emptyList<UserModel>())
    var sortBy by mutableStateOf(BY_FAMILY)
    var search by mutableStateOf("")
    var searchResult = USER_LIST_IS_EMPTY
    var computers by mutableStateOf(emptyList<PCModel>())
    var title = ""
        private set
    var position by mutableIntStateOf(0)

    init {
        val args = savedStateHandle.toRoute<PCListDestination>()

        /*viewModelScope.launch {
            userRepository.userList().collect { list ->
                users = list.sortedWith(
                    compareBy<UserModel> { it.family }
                        .thenBy { it.name }
                        .thenBy { it.patronymic }
                )
            }
        }
        viewModelScope.launch {
            pcRepository.pcList().collect { pcList ->
                computers = pcList.sortedBy { it.userId }

                users = setUserPCList(users.toMutableList())

                foundUsers = users
                title = setTitle(USERS, foundUsers.size, users.size)
            }
        }*/

        viewModelScope.launch {
            pcRepository.pcList().collect { pcList ->
                computers = pcList.sortedBy { it.userId }
            }
        }
        viewModelScope.launch {
            userRepository.userList().collect { list ->
                users = list.sortedWith(
                    compareBy<UserModel> { it.family }
                        .thenBy { it.name }
                        .thenBy { it.patronymic }
                )
                users = setUserPCList(users.toMutableList())

                foundUsers = users
                title = setTitle(USERS, foundUsers.size, users.size)

                val idx = users.indexOf(users.find { it.id == args.id })
                if (idx > 0) {
                    position = idx
                }
            }
        }
    }

    private fun setUserPCList(users: MutableList<UserModel>): List<UserModel> {
        users.forEachIndexed { idx, user ->
            val pcList = computers.filter { it.userId == user.id }
            if (pcList.isNotEmpty()) {
                var list = ""
                pcList.forEach { pc ->
                    list += (if (list.isNotEmpty()) ", " else "") + pc.inventoryNumber
                }
                users[idx] = user.copy(pcList = list)
            }
        }
        return users
    }

    fun onEvent(event: UserListEvent) {
        when (event) {
            is UserListEvent.OnUserListSortByChange -> {
                search = ""
                foundUsers = users
                title = setTitle(USERS, foundUsers.size, users.size)

                sortBy = event.sortBy
                foundUsers = when (sortBy) {
                    BY_SERVICE_NUMBER -> {
                        foundUsers.sortedBy { it.serviceNumber }
                    }

                    BY_OFFICES -> {
                        foundUsers.sortedWith(
                            compareBy<UserModel> { it.office }
                                .thenBy { it.family }
                                .thenBy { it.name }
                                .thenBy { it.patronymic }
                        )
                    }

                    else -> {
                        foundUsers.sortedWith(
                            compareBy<UserModel> { it.family }
                                .thenBy { it.name }
                                .thenBy { it.patronymic }
                        )
                    }
                }
            }

            is UserListEvent.OnUserListSearchChange -> {
                search = event.search
                if (search.isBlank()) {
                    foundUsers = users
                    searchResult = USER_LIST_IS_EMPTY
                } else {
                    searchResult = FOUND_NOTHING
                    foundUsers = when (sortBy) {
                        BY_SERVICE_NUMBER -> {
                            users.filter { it.serviceNumber.contains(search, ignoreCase = true) }
                        }

                        BY_OFFICES -> {
                            users.filter { it.office.contains(search, ignoreCase = true) }
                        }

                        else -> {
                            users.filter {
                                it.family.contains(search, ignoreCase = true)
                                        || it.name.contains(search, ignoreCase = true)
                                        || it.patronymic.contains(search, ignoreCase = true)
                            }
                        }
                    }
                    foundUsers = setUserPCList(foundUsers.toMutableList())
                }
                title = setTitle(USERS, foundUsers.size, users.size)
            }

            is UserListEvent.OnUserListDelete -> {
                search = ""
                val userEntity = UserEntity(
                    id = event.user.id,
                    serviceNumber = event.user.serviceNumber,
                    family = event.user.family,
                    name = event.user.name,
                    patronymic = event.user.patronymic,
                    phone = event.user.phone,
                    officeId = event.user.officeId.toInt()
                )
                viewModelScope.launch {
                    userRepository.deleteUser(userEntity)
                    title = setTitle(USERS, foundUsers.size, users.size)
                }
            }
        }
    }
}