package com.ucne.tecnicostarea2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TiposTecnicos")
data class TipoTecnicoEntity(
    @PrimaryKey(autoGenerate = true)
    var tipoTecnicoId: Int?= null,
    var descripcion: String
)
