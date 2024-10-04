package js.apps.gatoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import js.apps.gatoapp.ui.theme.GatoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel by viewModels<GameViewModel>()

        setContent {
            val navController = rememberNavController()
            GatoAppTheme {
                NavHost(navController = navController, startDestination = "menu") {
                    composable("menu") {
                        MenuScreen(navController)
                    }
                    composable("game") {
                        GameScreen(viewModel, navController)
                    }
                }
            }
        }


    }
}

@Composable
fun MenuScreen(navController: NavController) {

    var dialogVisible by remember {
        mutableStateOf(false)
    }
    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Juego del gato", fontSize = 24.sp)
        Button(onClick = { navController.navigate("game") }) {
            Text(text = "Jugar")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { dialogVisible = true }) {
            Text(text = "Instrucciones")
        }
        if (dialogVisible){
            DialogoInstrucciones(){
                dialogVisible = false
            }
        }
        
    }
}

@Composable 
fun DialogoInstrucciones(
    onDismiss: () -> Unit
){
    
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Instrucciones", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(fontSize = 14.sp,
                    text = "Objetivo del juego: Ser el primer jugador en colocar tres de sus marcas (X u O) en una fila horizontal, vertical o diagonal en un tablero de 3x3.\n" +
                            "Cómo jugar:\n" +
                            "El tablero: El juego se juega en un tablero de 3x3 casillas.\n" +
                            "Los jugadores: Dos jugadores participan, uno usando \"X\" y el otro usando \"O\".\n" +
                            "Turnos: Los jugadores se turnan para colocar su marca en una casilla vacía del tablero.\n" +
                            "Ganar: El primer jugador en conseguir tres de sus marcas en una fila (horizontal, vertical o diagonal) gana la partida.\n" +
                            "Empate: Si todas las casillas del tablero están ocupadas y ningún jugador ha conseguido tres en raya, la partida termina en empate."
                )
            }

        }
    }
}