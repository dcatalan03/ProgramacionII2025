package com.example.clase3.modelo

/**
 * Clase abstracta Mascota
 * Representa una mascota genérica.
 */
abstract class Mascota(
    val nombre: String,
    val fechaNac: String,
    val numeroIdentificacion: String,
    val raza: String
) {
    abstract fun banar()
    abstract fun cepillar()
}

/**
 * Clase Gato, hereda de Mascota
 */
class Gato(
    nombre: String,
    fechaNac: String,
    numeroIdentificacion: String,
    raza: String
) : Mascota(nombre, fechaNac, numeroIdentificacion, raza) {
    override fun banar() {
        println("Bañando al gato $nombre")
    }
    override fun cepillar() {
        println("Cepillando al gato $nombre")
    }
    fun jugar() {
        println("El gato $nombre está jugando")
    }
}

/**
 * Clase Perro, hereda de Mascota
 */
class Perro(
    nombre: String,
    fechaNac: String,
    numeroIdentificacion: String,
    raza: String
) : Mascota(nombre, fechaNac, numeroIdentificacion, raza) {
    override fun banar() {
        println("Bañando al perro $nombre")
    }
    override fun cepillar() {
        println("Cepillando al perro $nombre")
    }
    fun pasear() {
        println("Paseando al perro $nombre")
    }
}
