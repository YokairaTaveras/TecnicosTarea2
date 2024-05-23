package com.ucne.tecnicostarea2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.ucne.tecnicostarea2.data.local.database.TecnicoDb
import com.ucne.tecnicostarea2.data.local.repository.TecnicoRepository
import com.ucne.tecnicostarea2.presentation.TecnicoScreen
import com.ucne.tecnicostarea2.presentation.TecnicoListScreen
import com.ucne.tecnicostarea2.presentation.TecnicoViewModel
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import com.ucne.tecnicostarea2.data.local.repository.TipoTecnicoRepository
import com.ucne.tecnicostarea2.presentation.Tipo.TipoTecnicoListScreen
import com.ucne.tecnicostarea2.presentation.Tipo.TipoTecnicoScreen
import com.ucne.tecnicostarea2.presentation.Tipo.TipoTecnicoViewModel

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tecnicoDb = Room.databaseBuilder(
            this,
            TecnicoDb::class.java,
            "Tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())
        val repository2 = TipoTecnicoRepository(tecnicoDb.tipoTecnicoDao())
        enableEdgeToEdge()
        setContent {
            TecnicosTarea2Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.TecnicoList) {

                    composable<Screen.TecnicoList> {
                        TecnicoListScreen(
                            viewModel = viewModel { TecnicoViewModel(repository, 0) },
                            onVerTecnico = {
                                navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                            },
                            onEliminarTecnico = {},
                            onGuardarTecnico = { },
                            navController = navController,
                            onAddTecnico = {}

                            )

                    }
                    composable<Screen.Tecnico> {
                        val args = it.toRoute<Screen.Tecnico>()
                        TecnicoScreen(
                            viewModel = viewModel { TecnicoViewModel(repository, args.tecnicoId) },
                            navController = navController
                        )
                    }
                    composable<Screen.TipoTecnico> {
                        val args = it.toRoute<Screen.TipoTecnico>()
                        TipoTecnicoScreen(viewModel = viewModel {
                            TipoTecnicoViewModel(
                                repository2,
                                args.tipoTecnicoId
                            )
                        },
                            navController = navController)
                    }

                    composable<Screen.TipoTecnicoList> {
                        TecnicoListScreen(
                            viewModel = viewModel { TecnicoViewModel(repository, 0) },
                            onVerTecnico = {navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))} ,
                            onGuardarTecnico = {navController.navigate(Screen.Tecnico(0))},
                            navController = navController,
                            onEliminarTecnico ={},
                            onAddTecnico = {},
                            )

                    }
                }
            }
        }
    }
}

sealed class Screen {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

    @Serializable
    object TipoTecnicoList : Screen()

    @Serializable
    data class TipoTecnico(val tipoTecnicoId: Int) : Screen()
}


/*@Preview(showBackground = true)
@Composable
fun Preview() {
    TecnicosTarea2Theme {

    }
}*/