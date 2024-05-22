package com.ucne.tecnicostarea2.data.local.repository

import com.ucne.tecnicostarea2.data.local.dao.TecnicoDao
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity

class TecnicoRepository(private val tecnicoDao: TecnicoDao) {
    suspend fun saveTecnico(tecnico: TecnicoEntity) = tecnicoDao.save(tecnico)
    suspend fun deleteTecnico(tecnico: TecnicoEntity) = tecnicoDao.delete(tecnico)
    suspend fun getTecnico(id: Int) = tecnicoDao.find(id)

    fun getTecnico() = tecnicoDao.getAll()
}