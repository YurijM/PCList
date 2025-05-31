package com.mu.pclist.data.repository

import com.mu.pclist.data.dao.UserDao
import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.model.UserModel
import com.mu.pclist.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val dao: UserDao
) : UserRepository {
    override suspend fun insert(user: UserEntity): Long {
        return dao.insert(user)
    }

    override suspend fun update(user: UserEntity): Int {
        return dao.update(user)
    }

    override suspend fun deleteUser(user: UserEntity) {
        dao.deleteUser(user)
    }

    override fun userList(): Flow<List<UserModel>> {
        return dao.userList()
    }

    override fun user(id: Long): Flow<UserEntity> {
        return dao.user(id)
    }
}
