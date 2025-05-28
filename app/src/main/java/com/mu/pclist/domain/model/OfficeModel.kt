package com.mu.pclist.domain.model

data class OfficeModel(
    val id: Long = 0,
    val code: String = "",
    val office: String = "",
    val shortName: String = "",
    val userId: Long = 0,
    val serviceNumber: String = "",
    val family: String = "",
    val name: String = "",
    val patronymic: String = "",
)
