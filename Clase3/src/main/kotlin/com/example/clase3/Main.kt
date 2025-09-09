package com.example.clase3

fun main() {
    println("*** DEMOSTRACIÓN BASADA EN EL DIAGRAMA DE CLASES ***")

    // Crear propietario
    val propietario = com.example.clase3.modelo.Propietario(
        nombre = "Juan Pérez",
        telefono = "5555-1234",
        direccion = "Zona 1, Ciudad",
        nit = "1234567-8"
    )

    // Crear mascotas
    val gato = com.example.clase3.modelo.Gato(
        nombre = "Michi",untitled
        fechaNac = "2022-01-10",
        numeroIdentificacion = "G123",
        raza = "Siames"
    )
    val perro = com.example.clase3.modelo.Perro(
        nombre = "Firulais",
        fechaNac = "2021-05-20",
        numeroIdentificacion = "P456",
        raza = "Labrador"
    )

    // Propietario deja mascotas
    propietario.dejarMascota(gato)
    propietario.dejarMascota(perro)

    // Crear cuidadores y asignar mascotas
    val cuidador1 = com.example.clase3.modelo.Cuidador("Carlos", "5555-6789", "Zona 2")
    val cuidador2 = com.example.clase3.modelo.Cuidador("María", "5555-9876", "Zona 3")
    cuidador1.asignar(gato)
    cuidador2.asignar(perro)
    cuidador1.supervisar()
    cuidador2.alimentar(perro)

    // Crear reservación e incluir mascotas
    val reservacion = com.example.clase3.modelo.Reservacion(
        fechaIngreso = "2025-08-01",
        fechaEgreso = "2025-08-05",
        numero = 1,
        costo = 500.0,
        noHabitacion = "A101"
    )
    reservacion.mascotas.add(gato)
    reservacion.mascotas.add(perro)

    // Agregar servicios extra
    val extra1 = com.example.clase3.modelo.ServicioExtra("Baño especial", "Baño con shampoo premium", 100.0)
    val extra2 = com.example.clase3.modelo.ServicioExtra("Corte de uñas", "Corte y limado de uñas", 50.0)
    reservacion.agregarExtra(extra1)
    reservacion.agregarExtra(extra2)

    // Propietario paga la reservación
    propietario.pagar(reservacion)

    // Métodos específicos de mascotas
    gato.banar()
    gato.cepillar()
    gato.jugar()
    perro.banar()
    perro.cepillar()
    perro.pasear()

    // Estado de reservación
    reservacion.finalizar()
}
