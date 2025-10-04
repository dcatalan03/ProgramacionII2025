package com.example.clase7.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.clase7.models.User

object FirestoreUtils {
    private val db = FirebaseFirestore.getInstance()
    private const val USERS_COLLECTION = "users"

    suspend fun isUserAllowedToRegister(email: String): Boolean {
        return try {
            val querySnapshot = db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email.lowercase())
                .whereEqualTo("canRegister", true)
                .get()
                .await()
            
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return try {
            val querySnapshot = db.collection(USERS_COLLECTION)
                .whereEqualTo("email", email.lowercase())
                .get()
                .await()
            
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
