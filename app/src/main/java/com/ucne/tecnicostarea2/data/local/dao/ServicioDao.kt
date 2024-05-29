package com.ucne.tecnicostarea2.data.local.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.tecnicostarea2.data.local.entities.ServicioEntity
import kotlinx.coroutines.flow.Flow

interface ServicioDao {
    @Upsert()
    suspend fun save(servicio: ServicioEntity)

    @Query(
        """
        SELECT * 
        FROM ServiciosTecnicos
        WHERE servicioId=:id
        LIMIT 1
        """
    )
    suspend fun find(id: Int): ServicioEntity?

    @Delete
    suspend fun delete(servicio: ServicioEntity)

    @Query("SELECT * FROM ServiciosTecnicos")
    fun getAll(): Flow<List<ServicioEntity>>
}