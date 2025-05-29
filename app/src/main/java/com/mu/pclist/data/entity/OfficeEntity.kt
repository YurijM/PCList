package com.mu.pclist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Types.NULL

@Entity(
    tableName = "table_offices",
    indices = [
        Index(
            value = ["code"],
            unique = true
        ),
        Index(
            value = ["office"],
            unique = true
        ),
        Index(
            value = ["short_name"],
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

data class OfficeEntity(
    @PrimaryKey(true) val id: Long = 0,
    val code: String = "",
    val office: String = "",
    @ColumnInfo(name = "short_name") val shortName: String = "",
    @ColumnInfo(name = "user_id", defaultValue = "NULL") val userId: Int? = NULL
)
