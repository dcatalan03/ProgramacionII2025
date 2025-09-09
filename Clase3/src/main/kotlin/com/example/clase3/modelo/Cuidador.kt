package com.example.clase3.modelo

/**
 * Clase Cuidador
 * Puede asignarse a cuidar mascotas y realizar acciones sobre ellas.
 */
class Cuidador(
    val nombre: String,
    val telefono: String,
    val direccion: String
) {
    val mascotasAsignadas = mutableListOf<Mascota>()

    fun asignar(mascota: Mascota) {
        mascotasAsignadas.add(mascota)
        println("$nombre ha sido asignado a la mascota ${mascota.nombre}")
    }

    fun supervisar() {
        println("$nombre está supervisando a sus mascotas asignadas.")
    }

    fun alimentar(mascota: Mascota) {
        println("$nombre está alimentando a ${mascota.nombre}")
    }
}
