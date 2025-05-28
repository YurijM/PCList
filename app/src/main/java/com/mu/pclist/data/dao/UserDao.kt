package com.mu.pclist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mu.pclist.data.entity.UserEntity
import com.mu.pclist.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(user: UserEntity): Int

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("UPDATE table_pc " +
            "SET user_id = NULL " +
            "WHERE user_id = :userId")
    suspend fun setUserNull(userId: Long)

    @Transaction
    suspend fun deleteUser(user: UserEntity) {
        setUserNull(user.id)
        delete(user)
    }

    @Query("SELECT u.id, u.service_number AS serviceNumber, u.family, u.name, u.patronymic, " +
            "o.id AS officeId, o.short_name AS office " +
            "FROM table_users u " +
            "INNER JOIN table_offices o ON o.id = u.office_id " +
            "ORDER BY u.service_number")
    fun userList() : Flow<List<UserModel>>
}