package com.mu.pclist.presentation.screen.user.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var users = userRepository.userList()

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
        }
    }
}