package com.mu.pclist.domain.model

data class PCModel(
    val id: Long = 0,
    val inventoryNumber: String = "",
    val userId: Long = 0,
    val serviceNumber: String = "",
    val family: String = "",
    val name: String = "",
    val patronymic: String = "",
    val officeId: Long = 0,
    val office: String = ""
)
