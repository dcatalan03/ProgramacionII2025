package com.example.clase3.modelo

/**
 * Clase ServicioExtra
 * Representa servicios adicionales que pueden agregarse a una reservación.
 */
data class ServicioExtra(
    val nombre: String,
    val descripcion: String,
    val precio: Double
)
