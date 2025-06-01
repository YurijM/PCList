package com.mu.pclist.data.repository

import com.mu.pclist.data.dao.PCDao
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.model.PCModel
import com.mu.pclist.domain.repository.PCRepository
import kotlinx.coroutines.flow.Flow

class PCRepositoryImpl(
    private val dao: PCDao
) : PCRepository {
    override suspend fun insert(pc: PCEntity): Long {
        return dao.insert(pc)
    }

    override suspend fun update(pc: PCEntity): Int {
        return dao.update(pc)
    }

    override suspend fun delete(pc: PCEntity) {
        dao.delete(pc)
    }

    override fun pcList(): Flow<List<PCModel>> {
        return dao.pcList()
    }

    override fun pc(id: Long): Flow<PCEntity> {
        return dao.pc(id)
    }
}