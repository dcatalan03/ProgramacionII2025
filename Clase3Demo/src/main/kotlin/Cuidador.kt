package org.umg

class Cuidador (
    val nombre: String,
    val apellido: String,
    val direccion: String
){
    val mascotasAsignadas = mutableListOf<Mascota>()

    fun asignar(mascota: Mascota){
        if (mascotasAsignadas.count() <= 4){
            mascotasAsignadas.add(mascota)
            println("${mascota.nombre} sera cuidado por $nombre")
        }
        else{
            println("El cuidador $nombre ya completo su cuota de mascotas")
        }
    }
    fun supervisar(){
        println("$nombre esta supervisando a sus mascotas")
    }
    fun alimentar(mascota: Mascota){
        val mascotaEncontrada = mascotasAsignadas.contains(mascota)
        if (mascotaEncontrada){
            println("$nombre esta siendo alimentando a ${mascota.nombre}")
        }
        else{
            println("La mascota no esta asignada a $nombre")
        }
    }
}