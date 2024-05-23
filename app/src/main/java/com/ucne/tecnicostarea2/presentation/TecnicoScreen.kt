package com.ucne.tecnicostarea2.presentation

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
import com.ucne.tecnicostarea2.presentation.components.TopAppBar
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme
import kotlinx.coroutines.launch

var nombreRepetido by mutableStateOf(false)
@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel,
    navController: NavHostController){
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        TecnicoBody(
            uiState = uiState,
            onSaveTecnico = { viewModel.saveTecnico() },
            onDeleteTecnico = { viewModel.deleteTecnico() },
            onNewTecnico = { viewModel.newTecnico() },
            onNombreChanged =  viewModel::onNombreChanged,
            onSueldoHoraChanged = viewModel::onSueldoHoraChanged,
            navController = navController
        )
    }

var sueldoHoraVacio by  mutableStateOf(false)
var nombreRepetidoVacio by  mutableStateOf(false)
@Composable
private fun TecnicoBody(
    uiState: TecnicoUIState,
    onSaveTecnico: () -> Unit,
    onDeleteTecnico: () -> Unit = {},
    onNombreChanged: (String) -> Unit,
    onSueldoHoraChanged: (String) -> Unit,
    onNewTecnico: () -> Unit,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                Modifier.requiredWidth(220.dp)
            ) {
                Text(text = "Tecnicos", modifier = Modifier.padding(16.dp))
                Divider()

                NavigationDrawerItem(
                    label = { Text(text = "Tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TipoTecnicoList) },
                    icon = {
                        Icon(imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Tecnicos")
                    })
            }


        },
        drawerState = drawerState) {
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = "Tecnicos",
            onDrawerClicked = {scope.launch { drawerState.open() }})
        }) { innerPadding ->

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
                    val nombreRepetido = false

                    OutlinedTextField(
                        label = { Text(text = "Nombres") },
                        value = uiState.nombres ?: "",
                        onValueChange = onNombreChanged,
                        modifier = Modifier.fillMaxWidth(),
                        isError = nombreRepetido || nombreRepetidoVacio
                    )
                    if (nombreRepetidoVacio) {
                        Text(
                            text = "El nombre no puede estar vacio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    if (nombreRepetido) {
                        Text(text = "El nombre ya existe", color = MaterialTheme.colorScheme.error)
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    OutlinedTextField(
                        label = { Text(text = "Sueldo por Hora") },
                        value = uiState.sueldoHora.toString(),
                        onValueChange = onSueldoHoraChanged,
                        modifier = Modifier.fillMaxWidth(),
                        isError = sueldoHoraVacio
                    )
                    if (sueldoHoraVacio) {
                        Text(
                            text = "El sueldo no puede estar vacio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
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
                                nombreRepetidoVacio = uiState.nombres.isNullOrBlank()
                                sueldoHoraVacio = uiState.sueldoHora.toString().isNullOrBlank()
                                if (nombreRepetidoVacio || sueldoHoraVacio || nombreRepetido) {

                                } else {
                                    onSaveTecnico(
                                    )
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
@Preview
@Composable
private fun TecnicoPreview() {
    TecnicosTarea2Theme {
        TecnicoBody(
            uiState = TecnicoUIState(),
            onSaveTecnico = {},
            onNombreChanged = {},
            onSueldoHoraChanged = {},
            onNewTecnico = {},
            onDeleteTecnico = {},
            navController = rememberNavController()
        )
    }
}

