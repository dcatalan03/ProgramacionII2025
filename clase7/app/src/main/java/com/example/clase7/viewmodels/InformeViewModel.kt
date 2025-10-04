package com.example.clase7.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clase7.models.Informe
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class InformeViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val auth = Firebase.auth

    private val _informes = MutableStateFlow<List<Informe>>(emptyList())
    val informes: StateFlow<List<Informe>> = _informes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        cargarInformes()
    }

    fun cargarInformes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val informesList = db.collection("informes")
                    .whereEqualTo("creadoPor", auth.currentUser?.uid ?: "")
                    .get()
                    .await()
                    .toObjects(Informe::class.java)
                _informes.value = informesList
            } catch (e: Exception) {
                _error.value = "Error al cargar los informes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun guardarInforme(
        curso: String,
        anio: Int,
        semestre: Int,
        fecha: Timestamp,
        comentarios: String,
        archivos: List<Uri> = emptyList()
    ): Boolean {
        return try {
            _isLoading.value = true
            
            // Subir archivos al storage
            val urlsArchivos = mutableListOf<String>()
            for (archivo in archivos) {
                val nombreArchivo = "${UUID.randomUUID()}.${obtenerExtension(archivo)}"
                val storageRef = storage.reference.child("informes/${auth.currentUser?.uid}/$nombreArchivo")
                
                // Subir el archivo
                val uploadTask = storageRef.putFile(archivo).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
                urlsArchivos.add(downloadUrl)
            }
            
            // Crear el objeto informe
            val informe = Informe(
                curso = curso,
                anio = anio,
                semestre = semestre,
                fecha = fecha,
                comentarios = comentarios,
                archivosAdjuntos = urlsArchivos,
                creadoPor = auth.currentUser?.uid ?: ""
            )
            
            // Guardar en Firestore
            db.collection("informes")
                .add(informe)
                .await()
                
            true
        } catch (e: Exception) {
            _error.value = "Error al guardar el informe: ${e.message}"
            false
        } finally {
            _isLoading.value = false
        }
    }
    
    private fun obtenerExtension(uri: Uri): String {
        val mimeType = Firebase.app.applicationContext.contentResolver.getType(uri)
        return mimeType?.substringAfterLast('/') ?: ""
    }
}
