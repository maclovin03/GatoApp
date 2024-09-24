package js.apps.gatoapp

data class Casilla(
    var posicion: Int,
    var ocupada: Boolean,
    var jugador: String ?= null

)
