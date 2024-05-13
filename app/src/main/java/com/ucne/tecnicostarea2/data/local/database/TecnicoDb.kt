package com.ucne.tecnicostarea2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.tecnicostarea2.data.local.dao.TecnicoDao
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TecnicoDb: RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
}
