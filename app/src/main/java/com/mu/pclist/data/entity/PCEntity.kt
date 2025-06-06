package com.mu.pclist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "table_pc",
    indices = [
        Index(
            value = ["inventory_number"],
            unique = true
        ),
    ]
)
data class PCEntity(
    @PrimaryKey(true) val id: Long = 0,
    @ColumnInfo(name = "inventory_number") val inventoryNumber: String = "",
)
