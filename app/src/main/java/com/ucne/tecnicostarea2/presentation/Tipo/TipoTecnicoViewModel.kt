package com.ucne.tecnicostarea2.presentation.Tipo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.tecnicostarea2.data.local.entities.TipoTecnicoEntity
import com.ucne.tecnicostarea2.data.local.repository.TipoTecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TipoTecnicoViewModel(private val repository: TipoTecnicoRepository, private val tipoTecnicoId: Int) : ViewModel() {

    var uiState = MutableStateFlow(TipoTecnicoUIState())
        private set

    val tipoTecnico = repository.getTipoTecnico()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onDescripcionChanged(descripcion: String?) {
        uiState.update {
            it.copy(descripcion = descripcion.toString())
        }
    }

    init {
        viewModelScope.launch {
            val tipoTecnico = repository.getTipoTecnico(tipoTecnicoId)

            tipoTecnico?.let {
                uiState.update {
                    it.copy(
                        tipoTecnicoId = tipoTecnico.tipoTecnicoId  ?: 0,
                       descripcion = tipoTecnico.descripcion?: ""
                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            if(tipoTecnico.value.any { it.descripcion == uiState.value.descripcion && it.tipoTecnicoId != tipoTecnicoId }){
                repository.saveTipoTecnico(uiState.value.toEntity())
                uiState.value = TipoTecnicoUIState()
            }
        }
    }

    fun newTecnico() {
        viewModelScope.launch {
            uiState.value = TipoTecnicoUIState()
        }
    }

    fun deleteTecnico() {
        viewModelScope.launch {
            repository.deleteTipoTecnico(uiState.value.toEntity())
        }
    }


}

data class TipoTecnicoUIState(
    var descripcion: String = "",
    var tipoTecnicoId: Int? = null,
    var descripcionError: String? = null,
    var descripcionVacia: Boolean = false,
    var descripcionRepetidaVacia: Boolean = false
)

fun TipoTecnicoUIState.toEntity() = TipoTecnicoEntity(
   tipoTecnicoId = tipoTecnicoId,
    descripcion = descripcion,
)


/*class TecnicoViewModel(private val context: Context, private val repository: TecnicoRepository) : ViewModel() {
    private val _tecnico: MutableStateFlow<List<TecnicoEntity>> = MutableStateFlow(emptyList())
    val tipoTecnico: MutableStateFlow<List<TecnicoEntity>> = _tecnico

    private val _nombreRepetido = mutableStateOf(false)
    val nombreRepetido: MutableState<Boolean> = _nombreRepetido

    init {
        viewModelScope.launch {
            repository.getTipoTecnico().collect {
                _tecnico.value = it
            }
        }
    }

    fun saveTecnico(tipoTecnico: TecnicoEntity): Boolean {
        if (!validarTecnico(tipoTecnico)) {
            return false
        }

        viewModelScope.launch {
            repository.saveTecnico(tipoTecnico)
        }
        return true
    }

    fun deleteTecnico(tipoTecnico: TecnicoEntity) {
        viewModelScope.launch {
            repository.deleteTecnico(tipoTecnico)
        }
    }

    private fun validarTecnico(tipoTecnico: TecnicoEntity): Boolean {
        if (tipoTecnico.nombres.isNullOrEmpty() || tipoTecnico.sueldoHora == null) {

            return false
        }

        val nombreExistente =
            _tecnico.value.any { it.nombres.equals(tipoTecnico.nombres, ignoreCase = true) }
        if (nombreExistente) {
            _nombreRepetido.value = true
            return false
        }

        return true
    }

    companion object {
        fun provideFactory(context: Context, repository: TecnicoRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TecnicoViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return TecnicoViewModel(context, repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}*/
