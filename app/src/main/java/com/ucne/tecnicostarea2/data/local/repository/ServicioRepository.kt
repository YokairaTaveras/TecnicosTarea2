package com.ucne.tecnicostarea2.data.local.repository

import com.ucne.tecnicostarea2.data.local.dao.ServicioDao
import com.ucne.tecnicostarea2.data.local.entities.ServicioEntity

class ServicioRepository (private val servicioDao: ServicioDao) {
    suspend fun saveServicio(servicio: ServicioEntity) = servicioDao.save(servicio)

    fun getServicio() = servicioDao.getAll()

    suspend fun deleteServico(servicio: ServicioEntity){
        servicioDao.delete(servicio)
    }

    suspend fun getServicio(servicioId:Int) = servicioDao.find(servicioId)}