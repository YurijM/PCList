package com.mu.pclist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "table_users",
    indices = [
        Index(
            value = ["service_number"],
            unique = true
        ),
        Index(
            value = ["family", "name", "patronymic"],
            unique = true
        ),
        Index(
            value = ["office_id"]
        ),
    ],
    foreignKeys = [
        ForeignKey(
            entity = OfficeEntity::class,
            parentColumns = ["id"],
            childColumns = ["office_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserEntity(
    @PrimaryKey(true) val id: Long = 0,
    @ColumnInfo(name = "service_number") val serviceNumber: String = "",
    val family: String = "",
    val name: String = "",
    val patronymic: String = "",
    val phone: String = "",
    @ColumnInfo(name = "office_id", defaultValue = "NULL") val officeId: Int? = null,
)
