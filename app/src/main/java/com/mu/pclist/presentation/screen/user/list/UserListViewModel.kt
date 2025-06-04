package com.mu.pclist.presentation.screen.user.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.model.UserModel
import com.mu.pclist.domain.repository.UserRepository
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.BY_SERVICE_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    //var users = userRepository.userList()
    var users by mutableStateOf(emptyList<UserModel>())
    var sortBy by mutableStateOf(BY_FAMILY)

    init {
        viewModelScope.launch {
            userRepository.userList().collect { list ->
                users = list.sortedWith(
                    compareByDescending<UserModel> { it.family }
                        .thenBy { it.name }
                        .thenBy { it.patronymic }
                )
            }
        }
    }

    fun onEvent(event: UserListEvent) {
        when (event) {
            is UserListEvent.OnUserListDelete -> {
                viewModelScope.launch {
                    userRepository.deleteUser(
                        UserEntity(
                            id = event.user.id,
                            serviceNumber = event.user.serviceNumber,
                            family = event.user.family,
                            name = event.user.name,
                            patronymic = event.user.patronymic,
                            officeId = event.user.officeId.toInt()
                        )
                    )
                }
            }

            is UserListEvent.OnUserListSortByChange -> {
                sortBy = event.sortBy
                when (sortBy) {
                    BY_SERVICE_NUMBER -> {
                        users.sortedBy { it.serviceNumber }
                    }
                    BY_OFFICES -> {
                        users.sortedBy { it.office }
                    }
                    else -> {
                        users.sortedWith(
                            compareByDescending<UserModel> { it.family }
                                .thenBy { it.name }
                                .thenBy { it.patronymic }
                        )
                    }
                }
            }
        }
    }
}