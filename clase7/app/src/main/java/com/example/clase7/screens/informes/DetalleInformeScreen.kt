package com.example.clase7.screens.informes

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.clase7.viewmodels.InformeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleInformeScreen(
    navController: NavController,
    viewModel: InformeViewModel,
    informeId: String
) {
    val context = LocalContext.current
    var informe by remember { mutableStateOf<com.example.clase7.models.Informe?>(null) }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Cargar los detalles del informe
    LaunchedEffect(informeId) {
        try {
            val doc = Firebase.firestore.collection("informes").document(informeId).get().await()
            if (doc.exists()) {
                informe = doc.toObject(com.example.clase7.models.Informe::class.java)
            }
        } catch (e: Exception) {
            // Manejar error
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Informe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading && informe == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (informe == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontró el informe solicitado")
            }
        } else {
            val informeDetalle = informe!!
            val fechaFormateada = remember(informeDetalle.fecha) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                sdf.format(informeDetalle.fecha.toDate())
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Encabezado con información básica
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Curso",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = informeDetalle.curso,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Fecha",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("Año: ${informeDetalle.anio} - Semestre: ${informeDetalle.semestre}")
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Fecha",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("Fecha: $fechaFormateada")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de comentarios
                if (informeDetalle.comentarios.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notes,
                                    contentDescription = "Comentarios",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Comentarios",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(informeDetalle.comentarios)
                        }
                    }
                }

                // Archivos adjuntos
                if (informeDetalle.archivosAdjuntos.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Attachment,
                                    contentDescription = "Archivos adjuntos",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Archivos Adjuntos (${informeDetalle.archivosAdjuntos.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            informeDetalle.archivosAdjuntos.forEachIndexed { index, url ->
                                val fileName = url.substringAfterLast('/').substringBefore('?')
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Abrir el archivo
                                            try {
                                                val intent = android.content.Intent(
                                                    android.content.Intent.ACTION_VIEW,
                                                    android.net.Uri.parse(url)
                                                )
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                // Manejar error al abrir el archivo
                                            }
                                        },
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.InsertDriveFile,
                                            contentDescription = "Archivo"
                                        )
                                        Text(
                                            text = fileName,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.OpenInNew,
                                            contentDescription = "Abrir"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mostrar errores
        error?.let { errorMessage ->
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(errorMessage)
            }
        }
    }
}
