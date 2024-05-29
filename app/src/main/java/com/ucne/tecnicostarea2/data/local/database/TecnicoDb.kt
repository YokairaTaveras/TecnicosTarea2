package com.ucne.tecnicostarea2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.tecnicostarea2.data.local.dao.ServicioDao
import com.ucne.tecnicostarea2.data.local.dao.TecnicoDao
import com.ucne.tecnicostarea2.data.local.dao.TipoTecnicoDao
import com.ucne.tecnicostarea2.data.local.entities.ServicioEntity
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.data.local.entities.TipoTecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TipoTecnicoEntity::class,
        ServicioEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class TecnicoDb: RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun tipoTecnicoDao(): TipoTecnicoDao
    abstract fun servicioDao(): ServicioDao
}