package org.umg

class Propietario (
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val nit : String
) {
    fun dejarMascota(){
        println("$nombre ha dejado su mascota")
    }
    fun pagar(reservacion: Reservacion){

    }
}