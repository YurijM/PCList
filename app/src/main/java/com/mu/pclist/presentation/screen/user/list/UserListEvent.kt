package com.mu.pclist.presentation.screen.user.list

import android.content.Context
import com.mu.pclist.domain.model.UserModel

sealed class UserListEvent {
    data class OnUserListSortByChange(val sortBy: String) : UserListEvent()
    data class OnUserListSearchChange(val search: String) : UserListEvent()
    data class OnUserListDelete(val user: UserModel) : UserListEvent()
    data class OnUserListDocCreate(val context: Context) : UserListEvent()
    data object OnUserListWithoutInternet : UserListEvent()
}