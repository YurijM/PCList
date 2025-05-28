package com.mu.pclist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.domain.model.OfficeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface OfficeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(office: OfficeEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(office: OfficeEntity): Int

    @Delete
    suspend fun delete(office: OfficeEntity)

    @Query("SELECT o.id, o.code, o.office, o.short_name AS shortName, " +
            "u.id AS userId, u.service_number AS serviceNumber, u.family, u.name, u.patronymic " +
            "FROM table_offices o " +
            "INNER JOIN table_users u ON u.id = o.user_id " +
            "ORDER BY o.code")
    fun officeList() : Flow<List<OfficeModel>>
}