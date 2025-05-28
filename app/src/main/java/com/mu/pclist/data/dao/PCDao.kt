package com.mu.pclist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.domain.model.PCModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PCDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pc: PCEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(pc: PCEntity): Int

    @Delete
    suspend fun delete(pc: PCEntity)

    @Query("SELECT p.id, p.inventory_number AS inventoryNumber, " +
            "o.id AS officeId, o.short_name as office, " +
            "u.id AS userId, u.service_number AS serviceNumber, u.family, u.name, u.patronymic " +
            "FROM table_pc p " +
            "INNER JOIN table_users u ON u.id = p.user_id " +
            "INNER JOIN table_offices o ON o.id = u.office_id " +
            "ORDER BY p.inventory_number")
    fun pcList(): Flow<List<PCModel>>
}