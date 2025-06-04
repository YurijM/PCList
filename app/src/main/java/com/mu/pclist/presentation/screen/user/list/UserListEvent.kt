package com.mu.pclist.presentation.screen.user.list

import com.mu.pclist.domain.model.UserModel

sealed class UserListEvent {
    data class OnUserListSortByChange(val sortBy: String) : UserListEvent()
    data class OnUserListSearchChange(val search: String) : UserListEvent()
    data class OnUserListDelete(val user: UserModel) : UserListEvent()
}