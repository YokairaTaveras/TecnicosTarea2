package com.ucne.tecnicostarea2.presentation

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ucne.tecnicostarea2.Screen
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.presentation.Tipo.TipoTecnicoViewModel
import com.ucne.tecnicostarea2.presentation.components.FloatingActionButtonSimple
import com.ucne.tecnicostarea2.presentation.components.TopAppBar
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme
import kotlinx.coroutines.launch

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
    onEliminarTecnico: () -> Unit,
    onGuardarTecnico: () -> Unit,
    navController: NavHostController,
    onAddTecnico: () -> Unit

) {

    val tecnico by viewModel.tecnico.collectAsStateWithLifecycle()
    TecnicoListBody(
        tecnico = tecnico,
        onVerTecnico = onVerTecnico,
        onEliminarTecnico = onEliminarTecnico,
        navController = navController,
        onAddTecnico = onAddTecnico
    )

    }

    @Composable
    fun TecnicoListBody(
        tecnico: List<TecnicoEntity>,
        onVerTecnico: (TecnicoEntity) -> Unit,
        onEliminarTecnico: () -> Unit,
        navController: NavHostController,
        onAddTecnico: () -> Unit
    ) {
        val scope = rememberCoroutineScope()

        var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(drawerContent = { ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
            Text(text = "Tipos de tecnicos", modifier = Modifier.padding(16.dp))
            Divider()

            Divider()
            NavigationDrawerItem(label = { Text(text = "Tipos de tecnicos") },
                selected = false,
                onClick = { navController.navigate(Screen.TipoTecnicoList) },
                icon = {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Tipos de tecnicos")
                })

        }},
            drawerState = drawerState) {
        Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = "Tecnicos", onDrawerClicked = {scope.launch { drawerState.open() }}) },
            floatingActionButton = { FloatingActionButtonSimple (
            onAddTecnico

            )}) { innerPadding ->

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
        TecnicoListBody(tecnico = tecnico, onVerTecnico = {}, onEliminarTecnico = {}, navController = rememberNavController(), onAddTecnico = {})
    }
}