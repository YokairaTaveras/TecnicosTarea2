package com.ucne.tecnicostarea2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ServiciosTecnicos")
data class ServicioEntity(
    @PrimaryKey
    val ServicioId: Int? = null,
    var Fecha: String = "",
    var TecnicoId: Int? = null,
    var Cliente: String = "",
    var Descripcion: String = "",
    var Total: Double = 0.0
)
