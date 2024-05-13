package com.ucne.tecnicostarea2.presentation

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme

@Composable
fun TicketScreen(
    viewModel: TecnicoViewModel
) {
    val tecnico by viewModel.tecnico.collectAsStateWithLifecycle()
    TecnicoBody(
        onSaveTecnico = { tecnico ->
            viewModel.saveTecnico(tecnico)
        }
    )
}

@Composable
fun TecnicoBody(onSaveTecnico: (TecnicoEntity) -> Unit) {
    var tecnicoId by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var sueldoHora by remember { mutableStateOf("") }

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
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                label = { Text(text = "SueldoHora") },
                value = sueldoHora,
                onValueChange = { sueldoHora = it },
                modifier = Modifier.fillMaxWidth()
            )

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
        TecnicoBody() {
        }
    }
}