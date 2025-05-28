package com.mu.pclist.domain.repository

import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.model.OfficeModel
import kotlinx.coroutines.flow.Flow

interface OfficeRepository {
    suspend fun insert(office: OfficeEntity): Long
    suspend fun update(office: OfficeEntity): Int
    suspend fun deleteOffice(office: OfficeEntity)
    fun officeList(): Flow<List<OfficeModel>>
}