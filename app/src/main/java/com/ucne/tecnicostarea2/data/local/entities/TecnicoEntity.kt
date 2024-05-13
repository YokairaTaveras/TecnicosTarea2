package com.ucne.tecnicostarea2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")
data class TecnicoEntity(
    @PrimaryKey
    var tecnicoId: Int?= null,
    var nombres: String? = null,
    var sueldoHora: Double?= null
)
