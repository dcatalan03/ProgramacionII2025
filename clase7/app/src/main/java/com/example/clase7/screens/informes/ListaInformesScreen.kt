package com.example.clase7.screens.informes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clase7.models.Informe
import com.example.clase7.viewmodels.InformeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaInformesScreen(
    navController: NavController,
    viewModel: InformeViewModel
) {
    val informes by viewModel.informes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Cargar informes al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarInformes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Informes") },
                actions = {
                    IconButton(onClick = { 
                        navController.navigate("nuevo_informe")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo Informe")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (isLoading && informes.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (informes.isEmpty()) {
                Text(
                    text = "No hay informes registrados",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(informes) { informe ->
                        InformeCard(informe = informe)
                    }
                }
            }

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeCard(informe: Informe) {
    val fechaFormateada = remember(informe.fecha) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(informe.fecha.toDate())
    }

    Card(
        onClick = { /* Navegar al detalle del informe */ },
        modifier = Modifier.fillMaxWidth()
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
                    contentDescription = "Curso"
                )
                Text(
                    text = informe.curso,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Fecha"
                )
                Text("Año: ${informe.anio} - Semestre: ${informe.semestre}")
                Spacer(modifier = Modifier.width(16.dp))
                Text(fechaFormateada)
            }

            if (informe.comentarios.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Comentarios"
                    )
                    Text(
                        text = informe.comentarios.take(100) + if (informe.comentarios.length > 100) "..." else "",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (informe.archivosAdjuntos.isNotEmpty()) {
                Text(
                    text = "${informe.archivosAdjuntos.size} archivo(s) adjunto(s)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
