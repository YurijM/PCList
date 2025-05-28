package com.mu.pclist.data.repository

import com.mu.pclist.data.dao.OfficeDao
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.model.OfficeModel
import com.mu.pclist.domain.repository.OfficeRepository
import kotlinx.coroutines.flow.Flow

class OfficeRepositoryImpl(
    private val dao: OfficeDao
) : OfficeRepository {
    override suspend fun insert(office: OfficeEntity): Long {
        return dao.insert(office)
    }

    override suspend fun update(office: OfficeEntity): Int {
        return dao.update(office)
    }

    override suspend fun deleteOffice(office: OfficeEntity) {
        dao.deleteOffice(office)
    }

    override fun officeList(): Flow<List<OfficeModel>> {
        return officeList()
    }
}