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
            "IFNULL(o.id, 0) AS officeId, IFNULL(o.short_name, '') as office, " +
            "IFNULL(u.id, 0) AS userId, IFNULL(u.service_number, '') AS serviceNumber, IFNULL(u.phone, '') AS phone, " +
            "IFNULL(u.family, '') AS family, IFNULL(u.name, '') AS name, IFNULL(u.patronymic, '') AS patronymic " +
            "FROM table_pc p " +
            "LEFT JOIN table_users u ON u.id = p.user_id " +
            "LEFT JOIN table_offices o ON o.id = u.office_id " +
            "ORDER BY p.inventory_number")
    fun pcList(): Flow<List<PCModel>>

    @Query("SELECT * FROM table_pc " +
            "WHERE id = :id")
    fun pc(id: Long): Flow<PCEntity>
}