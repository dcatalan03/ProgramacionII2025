package org.umg

import java.time.LocalDate
import java.time.format.DateTimeFormatter

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun solicitarDatosDePropietario() : Propietario{
    println("Ingrese el nombre del propietario")
    val nombre = readLine().toString()
    println("Ingrese el telefono de propietario")
    val telefono = readLine().toString()
    println("Ingrese la direccion de propietario")
    val direccion = readLine().toString()
    println("Ingrese el nit de propietario")
    val nit = readLine().toString()
    return Propietario(nombre,telefono,direccion, nit )
}

fun datosDeLaMascota(): Mascota {
    println("Ingrese el nombre de la mascota")
    val nombre = readLine().toString()
    println("Ingrese la fecha de nacimiento")
    val fecha = readLine().toString()
    println("Ingrese la identificacion de la mascota")
    val id = readLine().toString()
    println("Ingrese la raza de la mascota")
    val raza = readLine().toString()
    println("Ingrese la especie de la mascota G para gato y P para perro")
    val especie = readLine().toString()
    val mascota : Mascota
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val fechaIngreso = LocalDate.parse(fecha, formatter)
    if (especie == "G"){
        mascota = Gato(nombre, fechaIngreso, id, raza)
    }else if (especie == "P"){
        mascota = Perro(nombre, fechaIngreso, id, raza)
    }
    return mascota
}


fun main() {

    val cuidadores = mutableListOf<Cuidador>()
    cuidadores.add(Cuidador("Diego", "Fernandez", "Jalapa"))
    cuidadores.add(Cuidador("Mercedez", "Vasquez","Jalapa"))
    cuidadores.add(Cuidador("Anibal", "Trigueros", "Jalapa"))

    var servicios = mutableListOf<ServicioExtra>()
    servicios.add(ServicioExtra("Masaje", "Masaje", 250.00f))
    servicios.add(ServicioExtra("Antipulgas", "Liquido antipulgas", 200.00f))


    do {
        println("Bienvenido al Hotel Mascotilandia")
        println("1. Reservacion")
        println("2. Asignar cuidador")
        println("3. Salir")

        println("Elija una opcion:")
        val opcion = readLine()?.toInt() ?: 0

        when (opcion) {
            1 -> {
                val propietario = solicitarDatosDePropietario()
                val mascota = datosDeLaMascota()
            }
        }
    }while(true)
}