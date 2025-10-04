package com.example.clase7.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Informe(
    @DocumentId
    val id: String = "",
    val curso: String = "",
    val anio: Int = 0,
    val semestre: Int = 1, // 1 o 2
    val fecha: Timestamp = Timestamp.now(),
    val comentarios: String = "",
    val archivosAdjuntos: List<String> = emptyList(),
    val creadoPor: String = "", // ID del usuario que creó el informe
    val fechaCreacion: Timestamp = Timestamp.now()
)
