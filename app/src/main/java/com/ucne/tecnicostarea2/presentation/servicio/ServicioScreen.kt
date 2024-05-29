package com.ucne.tecnicostarea2.presentation.servicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.tecnicostarea2.Screen
import com.ucne.tecnicostarea2.data.local.entities.ServicioEntity
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.presentation.components.Combobox
import com.ucne.tecnicostarea2.presentation.components.TopAppBar

import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


var tecnicoLleno by mutableStateOf(false)

@Composable
fun ServicioScreen(
    viewModel: ServicioViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val servicios by viewModel.servicios.collectAsStateWithLifecycle()

    val tecnicos by viewModel.tecnico.collectAsStateWithLifecycle()

    ServicioBody(
        uiState = uiState,
        onFechaChanged = viewModel::onFechaChanged,
        onClienteChanged = viewModel::onclienteChanged,
        onDescripcionChanged = viewModel::onDescripcionChanged,
        onTotalChanged = viewModel::onTotalChanged,
        onSaveServicio = { viewModel.saveServicio() },
        servicios = servicios,
        navController = navController,
        tecnicos = tecnicos,
        onTecnicoChanged = { viewModel::onTecnicoChanged }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioBody(
    uiState: ServicioUIState,
    onFechaChanged: (String) -> Unit,
    onClienteChanged: (String) -> Unit,
    onDescripcionChanged: (String) -> Unit,
    onTotalChanged: (String) -> Unit,
    onSaveServicio: () -> Unit,
    servicios: List<ServicioEntity>,
    navController: NavHostController,
    tecnicos: List<TecnicoEntity>,
    onTecnicoChanged: (Int) -> Unit
) {

    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }

    var verFecha by remember { mutableStateOf(false) }

    val state = rememberDatePickerState()

    val scope = rememberCoroutineScope()
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
                Text(text = "Registro de Servicios", modifier = Modifier.padding(16.dp))
                Divider()

                NavigationDrawerItem(
                    label = { Text(text = "Servicios") },
                    selected = false,
                    onClick = { navController.navigate(Screen.ServicioList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Servicios"
                        )
                    }
                )
            }
        },
        drawerState = drawerState
    ) {}

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Registro de Servicios Tecnicos",
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    ) { innerPadding ->

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
                        label = { Text(text = "Fecha") },
                        value = uiState.fecha,
                        readOnly = true,
                        onValueChange = onFechaChanged,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    verFecha = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Date Picker"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = true) {
                                verFecha = true
                            }
                    )

                    OutlinedTextField(
                        label = { Text(text = "Cliente") },
                        value = uiState.cliente ?: "",
                        onValueChange = onClienteChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Combobox(
                        label = "Tecnicos disponibles",
                        items = tecnicos,
                        selectedItem = tecnicoSeleccionado,
                        selectedItemString = {
                            it?.let {
                                "${it.nombres}"
                            } ?: ""
                        },
                        onItemSelected = {
                            onTecnicoChanged(it?.tecnicoId ?: 0)
                            tecnicoSeleccionado = it
                            uiState.tecnicoId = it?.tecnicoId
                        },
                        itemTemplate = { Text(text = it.nombres ?: "") },
                        isErrored = tecnicoLleno,
                    )

                    OutlinedTextField(
                        label = { Text(text = "Descripcion") },
                        value = uiState.descripcion ?: "",
                        onValueChange = onDescripcionChanged,
                        modifier = Modifier.fillMaxWidth()
                    )


                    OutlinedTextField(
                        label = { Text(text = "Total") },
                        value = uiState.total.toString().replace("null", ""),
                        onValueChange = onTotalChanged,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                tecnicoLleno = false
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
                                if (Validar(
                                        ServicioEntity(
                                            servicioTecnicoId = uiState.servicioTecnicoId,
                                            fecha = uiState.fecha,
                                            tecnicoId = uiState.tecnicoId,
                                            cliente = uiState.cliente,
                                            descripcion = uiState.descripcion,
                                            total = uiState.total
                                        ),
                                        lServicios = servicios
                                    )
                                ) {
                                    onSaveServicio()
                                    navController.navigate(Screen.ServicioList)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                            Text(text = "Guardar")
                        }

                        if(verFecha){
                            DatePickerDialog(
                                onDismissRequest = { verFecha = false },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            val fechaSeleccionada = state.selectedDateMillis

                                            if(fechaSeleccionada != null){
                                                val selectedInstant = Instant.ofEpochMilli(fechaSeleccionada)
                                                val selectedLocalDate = selectedInstant.atZone(ZoneId.systemDefault()).toLocalDate()
                                                uiState.fecha = selectedLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                            }
                                            verFecha = false
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Blue,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(text = "Aceptar")
                                    }
                                },
                                dismissButton = {
                                    OutlinedButton(onClick = {verFecha = false}) {
                                        Text(text = "Cancelar")
                                    }
                                }
                            ) {
                                DatePicker(state = state)
                            }
                        }


                    }

                }

            }
        }
    }
}

fun Validar(servicio: ServicioEntity?, lServicios: List<ServicioEntity>?) : Boolean {
    tecnicoLleno = servicio?.tecnicoId.toString().isNullOrEmpty() || servicio?.tecnicoId?.toString()
        ?.isBlank() ?: false

    return !tecnicoLleno
}

@Preview
@Composable
private fun ServicioPreview() {
    TecnicosTarea2Theme {
        ServicioBody(
            uiState = ServicioUIState(),
            onFechaChanged = {},
            onClienteChanged ={} ,
            onDescripcionChanged = {},
            onTotalChanged = {},
            onSaveServicio = {},
            servicios = emptyList(),
            navController = rememberNavController(),
            tecnicos = emptyList()
        ) {

        }

    }
}