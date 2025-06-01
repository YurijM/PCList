package com.mu.pclist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "table_pc",
    indices = [
        Index(
            value = ["inventory_number"],
            unique = true
        ),
        Index(
            value = ["user_id"]
        ),
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PCEntity(
    @PrimaryKey(true) val id: Long = 0,
    @ColumnInfo(name = "inventory_number") val inventoryNumber: String = "",
    @ColumnInfo(name = "user_id", defaultValue = "NULL") val userId: Int? = null
)
