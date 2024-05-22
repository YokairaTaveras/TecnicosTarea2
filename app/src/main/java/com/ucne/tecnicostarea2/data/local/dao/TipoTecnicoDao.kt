package com.ucne.tecnicostarea2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ucne.tecnicostarea2.data.local.entities.TipoTecnicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoTecnicoDao {
    @Upsert()
    suspend fun save(tipoTecnico: TipoTecnicoEntity)

    @Query(
        """
        SELECT * 
        FROM TipoTecnico 
        WHERE tipoTecnicoId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): TipoTecnicoEntity?

    @Delete
    suspend fun delete(tipoTecnico: TipoTecnicoEntity)

    @Query("SELECT * FROM TipoTecnico")
    fun getAll(): Flow<List<TipoTecnicoEntity>>
}