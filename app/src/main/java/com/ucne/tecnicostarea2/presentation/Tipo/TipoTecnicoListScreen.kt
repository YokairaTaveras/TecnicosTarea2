package com.ucne.tecnicostarea2.presentation.Tipo

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
import androidx.compose.material3.DrawerState
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
import com.ucne.tecnicostarea2.data.local.entities.TipoTecnicoEntity
import com.ucne.tecnicostarea2.presentation.components.TopAppBar
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme
import kotlinx.coroutines.launch

@Composable
fun TipoTecnicoListScreen(
    viewModel: TipoTecnicoViewModel,
    onVerTecnico: (TipoTecnicoEntity) -> Unit,
    onEliminarTecnico: () -> Unit,
    navController: NavHostController,
    onAddTipoTecnico: () -> Unit

) {

    val tipoTecnico by viewModel.tipoTecnico.collectAsStateWithLifecycle()
    TipoTecnicoListBody(
        tipoTecnico = tipoTecnico,
        onVerTecnico = onVerTecnico,
        onEliminarTecnico = onEliminarTecnico,
        navController = navController,
        onAddTipoTecnico = onAddTipoTecnico
    )

}

@Composable
fun TipoTecnicoListBody(
    tipoTecnico: List<TipoTecnicoEntity>,
    onVerTecnico: (TipoTecnicoEntity) -> Unit,
    onEliminarTecnico: () -> Unit,
    navController: NavHostController,
    onAddTipoTecnico: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet(Modifier.requiredWidth(220.dp)) {
            Text(text = "Tipos tecnicos", modifier = Modifier.padding(16.dp))
            Divider()

            NavigationDrawerItem(label = { Text(text = "Tipos tecnicos") },
                selected = false,
                onClick = { navController.navigate(Screen.TecnicoList) },
                icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Tipos tecnicos")})

        }

    },
        drawerState = drawerState
    ) {
            Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = { TopAppBar(title = "Tipos de tecnicos", onDrawerClicked = {
                scope.launch {
                    drawerState.open()
                }
            }) }) { innerPadding ->

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
                    items(tipoTecnico) { tipoTecnico ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVerTecnico(tipoTecnico) }
                                .padding(16.dp)
                        ) {

                            Text(text = tipoTecnico.descripcion ?: "", modifier = Modifier.weight(0.4f))
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
private fun TipoTecnicoListPreview() {
    val tipoTecnico = listOf(
        TipoTecnicoEntity(
            tipoTecnicoId = 1,
            descripcion = "Sistemas"
        ),
        TipoTecnicoEntity(
            tipoTecnicoId = 2,
            descripcion = "Soporte tecnico"
        ),
        TipoTecnicoEntity(
            tipoTecnicoId = 3,
            descripcion = "Mantenimiento de aire"
        ),
    )
    TecnicosTarea2Theme {
        TipoTecnicoListBody(tipoTecnico = tipoTecnico,
            onVerTecnico = {},
            onEliminarTecnico = {},
            navController = rememberNavController(),
            onAddTipoTecnico = {})
    }
}