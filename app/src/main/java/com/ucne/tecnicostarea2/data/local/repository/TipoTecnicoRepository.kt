package com.ucne.tecnicostarea2.data.local.repository

import com.ucne.tecnicostarea2.data.local.dao.TipoTecnicoDao
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.data.local.entities.TipoTecnicoEntity

class TipoTecnicoRepository(private val tipoTecnicoDao: TipoTecnicoDao) {
    suspend fun saveTipoTecnico(tipoTecnico: TipoTecnicoEntity) = tipoTecnicoDao.save(tipoTecnico)
    suspend fun deleteTipoTecnico(tipoTecnico: TipoTecnicoEntity) = tipoTecnicoDao.delete(tipoTecnico)
    suspend fun getTipoTecnico(id: Int) = tipoTecnicoDao.find(id)
    fun getTipoTecnico() = tipoTecnicoDao.getAll()
}
