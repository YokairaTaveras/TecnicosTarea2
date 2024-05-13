package com.ucne.tecnicostarea2.presentation

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.data.local.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TecnicoViewModel(private val context: Context, private val repository: TecnicoRepository) : ViewModel() {
    private val _tecnico: MutableStateFlow<List<TecnicoEntity>> = MutableStateFlow(emptyList())
    val tecnico: MutableStateFlow<List<TecnicoEntity>> = _tecnico

    private val _nombreRepetido = mutableStateOf(false)
    val nombreRepetido: MutableState<Boolean> = _nombreRepetido

    init {
        viewModelScope.launch {
            repository.getTecnico().collect {
                _tecnico.value = it
            }
        }
    }

    fun saveTecnico(tecnico: TecnicoEntity): Boolean {
        if (!validarTecnico(tecnico)) {
            return false
        }

        viewModelScope.launch {
            repository.saveTecnico(tecnico)
        }
        return true
    }

    fun deleteTecnico(tecnico: TecnicoEntity) {
        viewModelScope.launch {
            repository.deleteTecnico(tecnico)
        }
    }

    private fun validarTecnico(tecnico: TecnicoEntity): Boolean {
        if (tecnico.nombres.isNullOrEmpty() || tecnico.sueldoHora == null) {

            return false
        }

        val nombreExistente =
            _tecnico.value.any { it.nombres.equals(tecnico.nombres, ignoreCase = true) }
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
}
