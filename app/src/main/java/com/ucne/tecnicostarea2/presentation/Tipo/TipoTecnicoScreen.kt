package com.ucne.tecnicostarea2.presentation.Tipo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.tecnicostarea2.Screen
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme

var descripcionRepetida by mutableStateOf(false)

@Composable
fun TipoTecnicoScreen(
    viewModel: TipoTecnicoViewModel,
    navController: NavHostController
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TipoTecnicoBody(
        uiState = uiState,
        onSaveTecnico = { viewModel.saveTecnico() },
        onDeleteTecnico = { viewModel.deleteTecnico() },
        onNewTecnico = { viewModel.newTecnico() },
        onDescripcionChanged =  viewModel::onDescripcionChanged,
        navController = navController
    )
}
var descripcionVacia by mutableStateOf(false)
var descripcionRepetidaVacia by mutableStateOf(false)
@Composable
private fun TipoTecnicoBody(
    uiState: TipoTecnicoUIState,
    onSaveTecnico: () -> Unit,
    onDeleteTecnico: () -> Unit = {},
    onDescripcionChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
            Text(text = "Registro de tipos de tecnicos", modifier = Modifier.padding(16.dp))
            Divider()

            NavigationDrawerItem(label = { Text(text = "Tipos de tecnicos") },
                selected = false,
                onClick = { navController.navigate(Screen.TipoTecnicoList) },
                icon = {Icon(imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Tipos de tecnicos")}
            )

        }
    },
        drawerState = drawerState) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(8.dp)
                ) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {

                            OutlinedTextField(
                                label = { Text(text = "Descripcion") },
                                value = uiState.descripcion ?: "",
                                onValueChange = onDescripcionChanged,
                                modifier = Modifier.fillMaxWidth(),
                                isError = descripcionRepetida  || descripcionRepetidaVacia
                            )
                            if (descripcionRepetidaVacia) {
                                Text(
                                    text = "La descripción no puede estar vacia",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            if (descripcionRepetida) {
                                Text(text = "La descripción ya existe", color = MaterialTheme.colorScheme.error)
                            }
                            Spacer(modifier = Modifier.padding(2.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        descripcionVacia = false
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
                                        descripcionRepetidaVacia = uiState.descripcion.isBlank()
                                        if (descripcionRepetidaVacia || descripcionVacia) {

                                        } else {
                                            onSaveTecnico(
                                            )
                                            descripcionRepetidaVacia = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "save button"
                                    )
                                    Text(text = "Guardar")
                                }
                                OutlinedButton(
                                    onClick = {
                                        onDeleteTecnico()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete button"
                                    )
                                    Text(text = "Eliminar")
                                }
                            }
                        }
                    }

                }
            }

    }

}
@Preview
@Composable
private fun TipoTecnicoPreview() {
    TecnicosTarea2Theme {
        TipoTecnicoBody(
            uiState = TipoTecnicoUIState(),
            onSaveTecnico = {},
            onDescripcionChanged = {},
            onNewTecnico = {},
            onDeleteTecnico = {},
            navController = rememberNavController()
        )
    }
}

