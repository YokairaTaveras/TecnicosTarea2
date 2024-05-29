package com.ucne.tecnicostarea2.presentation.servicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.tecnicostarea2.data.local.entities.ServicioEntity
import com.ucne.tecnicostarea2.presentation.components.FloatingActionButton
import com.ucne.tecnicostarea2.presentation.components.TopAppBar
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme
import kotlinx.coroutines.launch


@Composable
fun ServicioListScreen(
    viewModel: ServicioViewModel,
    onVerServicio: (ServicioEntity) -> Unit,
    navController: NavHostController,
    onAgregarServicio: () -> Unit
) {
    val servicios by viewModel.servicios.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    ServicioListBody(
        servicios = servicios,
        onVerServicio = onVerServicio,
        onDeleteServicio = { servicio ->
            scope.launch { viewModel.deleteServicio(servicio) }
        },
        navController = navController,
        onAgregarServicio = onAgregarServicio
    )
}


@Composable
fun ServicioListBody(
    servicios: List<ServicioEntity>,
    onVerServicio: (ServicioEntity) -> Unit,
    onDeleteServicio: (ServicioEntity) -> Unit,
    navController: NavHostController,
    onAgregarServicio: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
                Text(
                    text = "Servicios",
                    modifier = Modifier.padding(16.dp)
                )
                Divider()

                NavigationDrawerItem(
                    label = { Text(text = "Tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Tipos de tecnicos"
                        )
                    }
                )

                Divider()

                NavigationDrawerItem(
                    label = { Text(text = "Tipos de tecnicos") },
                    selected = false,
                    onClick = { navController.navigate(Screen.TipoTecnicoList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Tipos de tecnicos"
                        )
                    }
                )
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Servicios") },
                    selected = false,
                    onClick = { navController.navigate(Screen.ServicioList) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "Servicios"
                        )
                    }
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Servicios",
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    title = "Agregar Servicio",
                    onAgregarServicio
                )
            }
        ) { innerPadding ->

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
                    items(servicios) { servicio ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVerServicio(servicio) }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = servicio.servicioTecnicoId.toString(),
                                modifier = Modifier.weight(0.10f)
                            )
                            Text(
                                text = servicio.fecha.toString(),
                                modifier = Modifier.weight(0.40f)
                            )
                            Text(
                                text = servicio.cliente.toString(),
                                modifier = Modifier.weight(0.30f)
                            )
                            Text(
                                text = servicio.total.toString(),
                                modifier = Modifier.weight(0.30f)
                            )
                            IconButton(onClick = { onDeleteServicio(servicio) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar Servicio"
                                )
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
private fun ServicioListPreview() {
    val servicios = listOf(
        ServicioEntity(
            cliente = "Yokaira Taveras",
            descripcion = "Servicio para darle mantenimiento a la computadora",
            total = 3000.00
        )
    )
    TecnicosTarea2Theme {
        ServicioListBody(
            servicios = servicios,
            onVerServicio = {},
            onDeleteServicio = {},
            navController = rememberNavController(),
            onAgregarServicio = {}
        )
    }
}