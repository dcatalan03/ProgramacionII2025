package org.umg

import java.time.LocalDate


class Reservacion(
    var fechaIngreso: LocalDate,
    var fechaEgreso: LocalDate? = null,
    val codigo: String,
    var costo: Float,
    var noHabitacion: Int,
    var estado: String
) {
    private val EstadoCancelada : String = "cancelada"
    private val EstadoFinalizada : String = "finalizada"

    private val serviciosExtra = mutableListOf<ServicioExtra>()

    fun cancelar(){
        estado = EstadoCancelada
    }
    fun finalizar(fechaEgreso: LocalDate, costo: Float){
        estado = EstadoFinalizada
        this.fechaEgreso = fechaEgreso
        this.costo = costo
    }
    fun reAgendar(nuevaFecha: LocalDate){
        fechaIngreso = nuevaFecha
    }
    fun agregarExtra(servicio: ServicioExtra){
        serviciosExtra.add(servicio)
    }

}