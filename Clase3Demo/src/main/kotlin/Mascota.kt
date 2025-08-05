package org.umg

import java.time.LocalDate

abstract class Mascota (
    val nombre: String,
    val fechaNacimiento: LocalDate,
    val identificacion: String,
    val raza: String,
)
{
    abstract fun banar()
    abstract fun cepillar()
}

class Perro (
    nombre: String,
    fechaNacimiento: LocalDate,
    identificacion: String,
    raza: String
) : Mascota (nombre, fechaNacimiento, identificacion, raza) {
    private var Pedigree: Boolean = false

    constructor(nombre: String,
                fechaNacimiento: LocalDate,
                identificacion: String,
                raza :String,
                pedigree :Boolean): this(nombre, fechaNacimiento, identificacion, raza)
    {
        this.Pedigree = pedigree
    }

    override fun banar(){
        println("Estamos banando al perro $nombre")
    }
    override fun cepillar(){
        println("Estamos cepillando al perro $nombre")
    }
    fun tienePedrigee(): Boolean{
        return this.Pedigree
    }
    fun solicitarPedigree(){
        this.Pedigree = true
    }
    fun pasear(){
        println("El perrito $nombre salio a pasear")
    }
}



class Gato (
    nombre: String,
    fechaNacimiento: LocalDate,
    identificacion: String,
    raza: String
) : Mascota (nombre, fechaNacimiento, identificacion, raza) {
    private var numeroBigotes: Int = 4

    constructor(nombre: String,
                fechaNacimiento: LocalDate,
                identificacion: String,
                raza :String,
                bigotes :Int): this(nombre, fechaNacimiento, identificacion, raza) {
        this.numeroBigotes = bigotes
    }

    override fun banar(){
        println("Estamos banando al gato $nombre")
    }
    override fun cepillar(){
        println("Estamos cepillando al gato $nombre")
    }
    fun cortarUnas() {
        println("Estamos cortando uñas a : $nombre")
    }
    fun obtenerBigotes() : Int{
        return this.numeroBigotes
    }
    fun jugar(){
        println("Estamos jugando $nombre")
    }
}
