package com.ucne.tecnicostarea2.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme

@Composable
fun TicketScreen(
    viewModel: TecnicoViewModel = viewModel()
) {
    var nombreRepetido by viewModel.nombreRepetido
    val tecnico by viewModel.tecnico.collectAsStateWithLifecycle()
    TecnicoBody(
        onSaveTecnico = { tecnico ->
            viewModel.saveTecnico(tecnico)
        },
        nombreRepetido = nombreRepetido
    )
}

var sueldoHoraVacio by  mutableStateOf(false)
var nombreRepetidoVacio by  mutableStateOf(false)
@Composable
fun TecnicoBody(onSaveTecnico: (TecnicoEntity) -> Unit, nombreRepetido: Boolean) {
    var tecnicoId by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var sueldoHora by remember { mutableStateOf("") }
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = "Nombres") },
                value = nombres,
                onValueChange = { nombres = it },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreRepetido || nombreRepetidoVacio
            )
            if(nombreRepetidoVacio){
                Text(text = "El nombre no puede estar vacio", color = MaterialTheme.colorScheme.error)
            }

            if(nombreRepetido){
                Text(text = "El nombre ya existe", color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                label = { Text(text = "SueldoHora") },
                value = sueldoHora,
                onValueChange = { sueldoHora = it },
                modifier = Modifier.fillMaxWidth(),
                isError =  sueldoHoraVacio
            )
            if( sueldoHoraVacio){
                Text(text = "El sueldo no puede estar vacio", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        tecnicoId = ""
                        nombres = ""
                        sueldoHora = ""
                        nombreRepetidoVacio = false
                        sueldoHoraVacio = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "new button"
                    )
                    Text(text = "Nuevo")
                }
                OutlinedButton(
                    onClick = {
                        nombreRepetidoVacio = nombres.isBlank()
                        sueldoHoraVacio = sueldoHora.isBlank()
                        if (nombreRepetidoVacio || sueldoHoraVacio || nombreRepetido) {
                            Toast.makeText(
                                context,
                                "Favor de rellenar todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onSaveTecnico(
                                TecnicoEntity(
                                    tecnicoId = tecnicoId.toIntOrNull(),
                                    nombres = nombres,
                                    sueldoHora = sueldoHora.toDoubleOrNull()
                                )
                            )
                            tecnicoId = ""
                            nombres = ""
                            sueldoHora = ""
                            nombreRepetidoVacio = false
                            sueldoHoraVacio = false
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "save button"
                    )
                    Text(text = "Guardar")
                }
            }
        }

    }

}

@Preview
@Composable
private fun TecnicoPreview() {
    TecnicosTarea2Theme {
        TecnicoBody(onSaveTecnico = {}, nombreRepetido = false)
    }
}
