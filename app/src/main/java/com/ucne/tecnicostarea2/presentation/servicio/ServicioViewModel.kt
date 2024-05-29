package com.ucne.tecnicostarea2.presentation.servicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.tecnicostarea2.data.local.entities.ServicioEntity
import com.ucne.tecnicostarea2.data.local.repository.ServicioRepository
import com.ucne.tecnicostarea2.data.local.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServicioViewModel(
    private val repository: ServicioRepository,
    private val servicioTecnicoId: Int,
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {
    var uiState = MutableStateFlow(ServicioUIState())
        private set

    val servicios = repository.getServicio()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val tecnico = tecnicoRepository.getTecnico()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onclienteChanged(cliente:String) {
        uiState.update {
            it.copy(cliente = cliente)
        }
    }
    fun onFechaChanged(fecha:String) {
        uiState.update {
            it.copy(fecha = fecha)
        }
    }
    fun onDescripcionChanged(descripcion:String) {
        uiState.update {
            it.copy(descripcion = descripcion)
        }
    }
    fun onTotalChanged(total:String) {
        uiState.update {
            it.copy(total = total.toDouble())
        }
    }

    fun onTecnicoChanged(tecnicoId: Int){
        uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }

    init {
        viewModelScope.launch {
            val servicio = repository.getServicio(servicioTecnicoId)

            servicio?.let {
                uiState.update {
                    it.copy(
                        servicioTecnicoId = servicio.servicioTecnicoId ?: 0,
                        fecha = servicio.fecha ?: "",
                        tecnicoId = servicio.tecnicoId ?: 0,
                        cliente = servicio.cliente ?: "",
                        descripcion = servicio.descripcion ?: "",
                        total = servicio.total ?: 0.0
                    )
                }
            }
        }
    }

    fun saveServicio(){
        viewModelScope.launch {
            repository.saveServicio(uiState.value.toEntity())
            uiState.value = ServicioUIState()
        }
    }

    suspend fun deleteServicio(servicio: ServicioEntity){
        repository.deleteServicio(servicio)
    }
}



data class ServicioUIState(
    val servicioTecnicoId: Int? = null,
    var fecha: String = "",
    var tecnicoId: Int? = null,
    var cliente: String? = null,
    var descripcion: String? = null,
    var total: Double = 0.0,

    var descripcionError: String? = null,

)

fun ServicioUIState.toEntity(): ServicioEntity{
    return ServicioEntity(
        servicioTecnicoId = servicioTecnicoId,
        fecha = fecha,
        tecnicoId = tecnicoId,
        cliente = cliente,
        descripcion = descripcion,
        total = total
    )
}