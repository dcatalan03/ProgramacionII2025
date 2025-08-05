package com.example.clase3.modelo

/**
 * Clase Propietario
 * Representa al dueño de una o varias mascotas.
 */
class Propietario(
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val nit: String
) {
    val mascotas = mutableListOf<Mascota>()

    fun dejarMascota(mascota: Mascota) {
        mascotas.add(mascota)
        println("${nombre} ha dejado a la mascota ${mascota.nombre}")
    }

    fun pagar(reservacion: Reservacion) {
        println("${nombre} ha pagado la reservación #${reservacion.numero} por Q${reservacion.costo}")
    }
}
