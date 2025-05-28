package com.mu.pclist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mu.pclist.data.dao.OfficeDao
import com.mu.pclist.data.dao.PCDao
import com.mu.pclist.data.dao.UserDao
import com.mu.pclist.data.entity.OfficeEntity
import com.mu.pclist.data.entity.PCEntity
import com.mu.pclist.data.entity.UserEntity

@Database(
    entities = [
        OfficeEntity::class,
        UserEntity::class,
        PCEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class PCDb : RoomDatabase() {
    abstract val officeDao: OfficeDao
    abstract val userDao: UserDao
    abstract val pcDao: PCDao
}