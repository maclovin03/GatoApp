package js.apps.gatoapp

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlin.random.Random

@Composable
fun GameScreen(gameViewModel: GameViewModel) {

    val listaCasillas by gameViewModel.gameTable.collectAsState()
    val context = LocalContext.current
    var firstTurn = 0
    var counter = 0
    val casillas = remember {
        mutableStateListOf<Casilla>()
    }
    for (i in 0..8) {
       casillas.add(Casilla(
            posicion = i,
            ocupada = false
        ))
    }
    var turno by remember { mutableStateOf((1..2).random()) }
    firstTurn = turno


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (matriz, canvas, turnoTxt) = createRefs()

        Text(text = if (turno == 1) "Turno de X" else "Turno de O")
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
            verticalArrangement = Arrangement.spacedBy(38.dp),
            contentPadding = PaddingValues(bottom = 8.dp),
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .constrainAs(matriz) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom, margin = 75.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }) {
            items(casillas) {
                Box(modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        if (!it.ocupada) {
                            if (turno) {
                                casillas.set(it.posicion, Casilla(it.posicion, true, "O"))
                                counter++
                                if(counter > 4) {


                                    if (chekIfWin(casillas)) {
                                        Toast
                                            .makeText(context, "Circulo gano", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            } else {
                                casillas.set(it.posicion, Casilla(it.posicion, true, "X"))
                            }
                            turno = !turno
                        }
                    }) {


                    if (it.ocupada) {
                        Image(
                            painter = if (it.jugador == "X") painterResource(id = R.drawable.cerrar)
                            else painterResource(id = R.drawable.circulo), contentDescription = null,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
            }
        }
    }



}

fun chekIfWin(casillas: List<Casilla>) : Boolean{

if (casillas[0].jugador == casillas[1].jugador && casillas[1].jugador == casillas[2].jugador){
    return true
}
    if (casillas[3].jugador == casillas[4].jugador && casillas[4].jugador == casillas[5].jugador){
        return true
    }
    if (casillas[6].jugador == casillas[7].jugador && casillas[7].jugador == casillas[8].jugador){
        return true
    }
    if (casillas[0].jugador == casillas[3].jugador && casillas[3].jugador == casillas[6].jugador){
        return true
    }

    if (casillas[1].jugador == casillas[4].jugador && casillas[4].jugador == casillas[7].jugador){
        return true
    }
    return false
}


