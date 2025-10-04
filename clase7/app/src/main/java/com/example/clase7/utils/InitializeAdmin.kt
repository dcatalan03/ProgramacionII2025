package com.example.clase7.utils

import com.google.firebase.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore

/**
 * Utilidad para inicializar un usuario administrador en Firestore.
 * Este es un script que se debe ejecutar una sola vez para configurar el usuario administrador inicial.
 */
suspend fun initializeAdminUser(adminEmail: String) {
    val db = Firebase.firestore
    val auth = Firebase.auth
    
    try {
        // Verificar si ya existe un usuario con este correo en Firestore
        val querySnapshot = db.collection("users")
            .whereEqualTo("email", adminEmail.lowercase())
            .get()
            .await()
        
        if (querySnapshot.isEmpty) {
            // Crear el documento del usuario en Firestore
            val adminUser = hashMapOf(
                "email" to adminEmail.lowercase(),
                "roles" to "admin",
                "canRegister" to true
            )
            
            // Agregar el documento a la colección 'users'
            db.collection("users")
                .add(adminUser)
                .addOnSuccessListener { documentReference ->
                    println("Usuario administrador creado con ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Error al crear usuario administrador: $e")
                }
        } else {
            println("El usuario administrador ya existe en la base de datos")
        }
    } catch (e: Exception) {
        println("Error al inicializar el usuario administrador: $e")
    }
}
