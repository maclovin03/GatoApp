package js.apps.gatoapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private val _gameTable = MutableStateFlow(emptyList<Casilla>().toMutableList())
    val gameTable: StateFlow<MutableList<Casilla>> = _gameTable.asStateFlow()

    init {

        for (i in 0..8) {
            _gameTable.value.add(Casilla(
                posicion = i,
                ocupada = false
            ))
        }
    }

    fun updateCasilla(posicion: Int, jugador: String) {
        _gameTable.value[posicion] = Casilla(
            posicion = posicion,
            ocupada = true,
            jugador = jugador
        )

    }
}