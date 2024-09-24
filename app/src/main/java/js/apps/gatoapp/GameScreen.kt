package js.apps.gatoapp

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import kotlin.random.Random

@Composable
fun GameScreen(gameViewModel: GameViewModel, navController: NavController) {

    val listaCasillas by gameViewModel.gameTable.collectAsState()
    val context = LocalContext.current
    val casillas = remember {
        mutableStateListOf<Casilla>()
    }
    var counter by remember {
        mutableIntStateOf(0)
    }
    var gameOver by remember {
        mutableStateOf(false)
    }
    var isTie by remember {
        mutableStateOf(false)
    }
    for (i in 0..8) {
       casillas.add(Casilla(
            posicion = i,
            ocupada = false
        ))
    }
    var turno by remember { mutableIntStateOf((1..2).random()) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val guideLine = createGuidelineFromBottom(0.3f)
        val (matriz, canvas, turnoTxt, winner, backBtn) = createRefs()

        Text(text = "Regresar", modifier = Modifier.constrainAs(backBtn) {
            top.linkTo(parent.top, margin = 30.dp)
            start.linkTo(parent.start, margin = 16.dp)
        }.clickable {
            navController.popBackStack()
            navController.navigate("menu")
        }, fontSize = 20.sp)


        Text(text = if (turno == 1) "Turno de X" else "Turno de O", modifier = Modifier.constrainAs(turnoTxt){
            top.linkTo(parent.top, margin = 100.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
        Canvas(modifier = Modifier
            .fillMaxSize()
            .constrainAs(canvas) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            drawLine(
                color = Color.Black,
                start = Offset(350f, 500f),
                end = Offset(350f, 1600f),
                strokeWidth = 4.dp.toPx()
            )
            drawLine(
                color = Color.Black,
                start = Offset(750f, 500f),
                end = Offset(750f, 1600f),
                strokeWidth = 4.dp.toPx()
            )
            drawLine(
                color = Color.Black,
                start = Offset(50f, 850f),
                end = Offset(1020f, 850f),
                strokeWidth = 4.dp.toPx()
            )
            drawLine(
                color = Color.Black,
                start = Offset(50f, 1250f),
                end = Offset(1020f, 1250f),
                strokeWidth = 4.dp.toPx()
            )
        }

        LazyVerticalGrid(columns = GridCells.Adaptive(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false,
            verticalArrangement = Arrangement.spacedBy(38.dp),
            contentPadding = PaddingValues(bottom = 8.dp),
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .constrainAs(matriz) {
                    top.linkTo(turnoTxt.bottom, margin = 75.dp)
                    bottom.linkTo(guideLine)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {
            items(casillas) {
                Box(modifier = Modifier
                    .size(100.dp)
                    .padding(start = 8.dp)
                    .clickable {
                        if (!it.ocupada && !gameOver) {
                            if (turno == 0) {
                                casillas[it.posicion] = Casilla(it.posicion, true, "O")
                                counter += 1
                                Log.w(TAG, counter.toString())
                                if (counter > 4) {
                                    Log.d(TAG, "se cumple")

                                    if (chekIfWin(casillas)) {
                                        Toast
                                            .makeText(context, "circulo gano", Toast.LENGTH_SHORT)
                                            .show()
                                        gameOver = true
                                    }else{
                                        if (counter == 9){
                                            Toast
                                                .makeText(context, "empate", Toast.LENGTH_SHORT)
                                                .show()
                                            isTie = true
                                            gameOver = true
                                        }
                                    }
                                }
                                turno = 1
                            } else {
                                casillas[it.posicion] = Casilla(it.posicion, true, "X")
                                counter += 1
                                Log.w(TAG, counter.toString())
                                if (counter > 4) {
                                    Log.d(TAG, "se cumple")

                                    if (chekIfWin(casillas)) {
                                        Toast
                                            .makeText(context, "equis gano", Toast.LENGTH_SHORT)
                                            .show()
                                        gameOver = true
                                    }else{
                                        if (counter == 9){
                                            Toast
                                                .makeText(context, "empate", Toast.LENGTH_SHORT)
                                                .show()
                                            isTie = true
                                            gameOver = true
                                        }
                                    }
                                }

                                turno = 0
                            }

                        }
                    }) {


                    if (it.ocupada) {
                        Image(
                            painter = if (it.jugador == "X") painterResource(id = R.drawable.cerrar)
                            else painterResource(id = R.drawable.circulo), contentDescription = null
                        )
                    }
                }
            }
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x6A202020))
            .constrainAs(winner) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                visibility =
                    if (!gameOver) androidx.constraintlayout.compose.Visibility.Gone else androidx.constraintlayout.compose.Visibility.Visible
            }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            if (isTie){
                Text(text = "¡Empate!", fontSize = 24.sp)
            }else {
                Text(
                    text = if (turno == 0) "¡El ganador es X!" else "¡El ganador es O!",
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            Button(onClick = {
                gameOver = false
                counter = 0
                turno = (1..2).random()
                for (i in 0..8) {
                    casillas[i] = Casilla(
                        posicion = i,
                        ocupada = false
                    )
                }
            }) {
                Text(text = "Volver a jugar")
            }
        }
    }



}

fun chekIfWin(casillas: List<Casilla>) : Boolean{


    if (casillas[0].jugador == casillas[1].jugador && casillas[1].jugador == casillas[2].jugador){
        if (casillas[0].jugador != null){
            Log.d(TAG, "se cumple 1")
            return true
        }
    }
    if (casillas[3].jugador == casillas[4].jugador && casillas[4].jugador == casillas[5].jugador){
            if (casillas[3].jugador != null){
                Log.d(TAG, "se cumple 2")
                return true
            }
        }
    if (casillas[6].jugador == casillas[7].jugador && casillas[7].jugador == casillas[8].jugador){
            if (casillas[6].jugador != null){
                Log.d(TAG, "se cumple 3")
                return true
            }
        }
    if (casillas[0].jugador == casillas[3].jugador && casillas[3].jugador == casillas[6].jugador){
            if (casillas[0].jugador != null){
                Log.d(TAG, "se cumple 4")
                return true
            }
        }

    if (casillas[1].jugador == casillas[4].jugador && casillas[4].jugador == casillas[7].jugador){
            if (casillas[1].jugador != null){
                Log.d(TAG, "se cumple 5")
                return true
            }
        }
    if (casillas[2].jugador == casillas[5].jugador && casillas[5].jugador == casillas[8].jugador){
            if (casillas[2].jugador != null){
                Log.d(TAG, "se cumple 6")
                return true
            }
    }
    if (casillas[0].jugador == casillas[4].jugador && casillas[4].jugador == casillas[8].jugador){
            if (casillas[0].jugador != null){
                Log.d(TAG, "se cumple 7")
                return true
            }
        }
    if (casillas[2].jugador == casillas[4].jugador && casillas[4].jugador == casillas[6].jugador){
            if (casillas[2].jugador != null){
                Log.d(TAG, "se cumple 8")
                return true
            }
    }

        return false
}


