package com.ucne.tecnicostarea2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.data.local.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class TecnicoViewModel(private val repository: TecnicoRepository, private val tecnicoId: Int) : ViewModel() {

    var uiState = MutableStateFlow(TecnicoUIState())
        private set

    val tecnico = repository.getTecnico()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onNombreChanged(nombres: String?) {
        uiState.update {
            it.copy(nombres = nombres)
        }
    }
    fun onSueldoHoraChanged(sueldoHora: String) {
        val letras = Regex("[a-zA-Z ]+")
        val numeros= sueldoHora.replace(letras, "").toDouble()
        uiState.update {
            it.copy(sueldoHora = numeros.toString())
        }
    }
    init {
        viewModelScope.launch {
            val tecnico = repository.getTecnico(tecnicoId)

            tecnico?.let {
                uiState.update {
                    it.copy(
                        tecnicoId = tecnico.tecnicoId  ?: 0,
                        nombres = tecnico.nombres?: "",
                        sueldoHora = tecnico.sueldoHora
                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            if(tecnico.value.any { it.nombres == uiState.value.nombres && it.tecnicoId != tecnicoId}){
                repository.saveTecnico(uiState.value.toEntity())
                uiState.value = TecnicoUIState()
            }
        }
    }

    fun newTecnico() {
        viewModelScope.launch {
            uiState.value = TecnicoUIState()
        }
    }

    fun deleteTecnico() {
        viewModelScope.launch {
            repository.deleteTecnico(uiState.value.toEntity())
        }
    }


}

data class TecnicoUIState(
    val tecnicoId: Int? = null,
    var nombres: String? = "",
    var nombreError: String? = null,
    var sueldoHora: String = null.toString(),
    var sueldoHoraError: String? = null,
)

fun TecnicoUIState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    nombres = nombres,
    sueldoHora = sueldoHora,
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
