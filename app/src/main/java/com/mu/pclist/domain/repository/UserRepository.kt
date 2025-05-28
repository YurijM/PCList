package com.mu.pclist.domain.repository

import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insert(user: UserEntity): Long
    suspend fun update(user: UserEntity): Int
    suspend fun delete(user: UserEntity)
    fun userList(): Flow<List<UserModel>>
}