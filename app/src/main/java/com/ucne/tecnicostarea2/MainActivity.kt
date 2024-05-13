package com.ucne.tecnicostarea2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.ucne.tecnicostarea2.data.local.database.TecnicoDb
import com.ucne.tecnicostarea2.data.local.entities.TecnicoEntity
import com.ucne.tecnicostarea2.data.local.repository.TecnicoRepository
import com.ucne.tecnicostarea2.presentation.TecnicoListScreen
import com.ucne.tecnicostarea2.presentation.TecnicoViewModel
import com.ucne.tecnicostarea2.presentation.TicketScreen
import com.ucne.tecnicostarea2.ui.theme.TecnicosTarea2Theme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())

        val viewModel = ViewModelProvider(
            this,
            TecnicoViewModel.provideFactory(applicationContext, repository)
        ).get(TecnicoViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            TecnicosTarea2Theme {
                Surface {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(8.dp)
                        ) {
                            TicketScreen(viewModel = viewModel)
                            TecnicoListScreen(
                                viewModel = viewModel,
                                onVerTecnico = {  },
                                onEliminarTecnico = { tecnico ->
                                    viewModel.deleteTecnico(tecnico)
                                    showNotification("Técnico eliminado")
                                },
                                onGuardarTecnico = { tecnico ->
                                    if (viewModel.saveTecnico(tecnico)) {
                                        showNotification("Técnico guardado")
                                    } else {
                                        showNotification("Nombre de técnico duplicado o campos incompletos")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showNotification(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    TecnicosTarea2Theme {

    }
}
