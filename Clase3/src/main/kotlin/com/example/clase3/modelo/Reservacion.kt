package com.example.clase3.modelo

/**
 * Clase Reservacion
 * Representa la reservación de una o varias mascotas, puede tener servicios extra.
 */
class Reservacion(
    val fechaIngreso: String,
    val fechaEgreso: String,
    val numero: Int,
    var costo: Double,
    val noHabitacion: String
) {
    val mascotas = mutableListOf<Mascota>()
    val serviciosExtra = mutableListOf<ServicioExtra>()
    var estado: String = "Agendada"

    fun cancelar() {
        estado = "Cancelada"
        println("Reservación #$numero cancelada.")
    }

    fun finalizar() {
        estado = "Finalizada"
        println("Reservación #$numero finalizada.")
    }

    fun agendar() {
        estado = "Agendada"
        println("Reservación #$numero agendada.")
    }

    fun agregarExtra(servicio: ServicioExtra) {
        serviciosExtra.add(servicio)
        costo += servicio.precio
        println("Servicio extra '${servicio.nombre}' agregado a la reservación #$numero.")
    }
}
