package com.mu.pclist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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

    @Query("UPDATE table_users " +
            "SET office_id = NULL " +
            "WHERE office_id = :officeId")
    suspend fun setOfficeNull(officeId: Long)

    @Transaction
    suspend fun deleteOffice(office: OfficeEntity) {
        setOfficeNull(office.id)
        delete(office)
    }

    @Query("SELECT o.id, o.code, o.office, o.short_name AS shortName, " +
            "IFNULL(u.id, 0) AS userId, IFNULL(u.service_number, '') AS serviceNumber, " +
            "IFNULL(u.family, '') AS family, IFNULL(u.name, '') AS name, IFNULL(u.patronymic, '') AS patronymic " +
            "FROM table_offices o " +
            "LEFT JOIN table_users u ON u.id = o.user_id " +
            "ORDER BY o.code")
    fun officeList() : Flow<List<OfficeModel>>

    @Query("SELECT * FROM table_offices " +
            "WHERE id = :id")
    fun office(id: Long): Flow<OfficeEntity>
}