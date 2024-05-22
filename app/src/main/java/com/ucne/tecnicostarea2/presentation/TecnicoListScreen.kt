package com.ucne.tecnicostarea2.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.presentation.components.TopAppBar
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onEliminarTecnico: () -> Unit,
    onGuardarTecnico: () -> Unit
) {

    val tecnico by viewModel.tecnico.collectAsStateWithLifecycle()
    TecnicoListBody(
        tecnico = tecnico,
        onVerTecnico = onVerTecnico,
        onEliminarTecnico = onEliminarTecnico
    )

    }

    @Composable
    fun TecnicoListBody(
        tecnico: List<TecnicoEntity>,
        onVerTecnico: (TecnicoEntity) -> Unit,
        onEliminarTecnico: () -> Unit
    ) {Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = "Tecnicos", onDrawerClicked = {}) }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(4.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(tecnico) { tecnico ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onVerTecnico(tecnico) }
                            .padding(16.dp)
                    ) {

                        Text(text = tecnico.nombres ?: "", modifier = Modifier.weight(0.4f))
                        Text(text = tecnico.sueldoHora.toString(), modifier = Modifier.weight(0.4f))
                        IconButton(onClick = { onEliminarTecnico() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
private fun TecnicoListPreview() {
    val tecnico = listOf(
        TecnicoEntity(
            nombres = "Yokaira Taveras",
            sueldoHora = "130.0"
        )
    )
    TecnicosTarea2Theme {
        TecnicoListBody(tecnico = tecnico, onVerTecnico = {}, onEliminarTecnico = {})
    }
}