package com.ucne.tecnicostarea2.data.local.repository

import com.ucne.tecnicostarea2.data.local.dao.TecnicoDao
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity

class TecnicoRepository(private val tecnicoDao: TecnicoDao) {
    suspend fun saveTecnico(tecnico: TecnicoEntity) = tecnicoDao.save(tecnico)

    fun getTecnico() = tecnicoDao.getAll()
}