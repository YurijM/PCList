package com.mu.pclist.domain.repository

import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.model.PCModel
import kotlinx.coroutines.flow.Flow

interface PCRepository {
    suspend fun insert(pc: PCEntity): Long
    suspend fun update(pc: PCEntity): Int
    suspend fun delete(pc: PCEntity)
    fun pcList(): Flow<List<PCModel>>
    fun pc(id: Long): Flow<PCEntity>
}