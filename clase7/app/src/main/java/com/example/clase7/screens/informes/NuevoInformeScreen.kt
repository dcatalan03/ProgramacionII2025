package com.example.clase7.screens.informes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.clase7.R
import com.example.clase7.viewmodels.InformeViewModel
import com.google.firebase.Timestamp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoInformeScreen(
    navController: NavController,
    viewModel: InformeViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Estados del formulario
    var curso by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var semestre by remember { mutableStateOf(1) }
    var fecha by remember { 
        mutableStateOf(
            Timestamp(
                Date.from(
                    LocalDate.now()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                )
            )
        ) 
    }
    var comentarios by remember { mutableStateOf("") }
    var archivos by remember { mutableStateOf<List<Uri>>(emptyList()) }
    
    // Estado para el diálogo de fecha
    val datePickerDialog = rememberMaterialDialogState()
    
    // Selector de archivos
    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                archivos = archivos + uris
            }
        }
    )
    
    // Formateador de fecha
    val fechaFormateada = remember(fecha) {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(fecha.toDate())
    }
    
    // Manejo de errores
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Mostrar diálogo de éxito
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(showSuccessDialog) {
        if (showSuccessDialog) {
            // Cerrar el diálogo después de 2 segundos y volver atrás
            kotlinx.coroutines.delay(2000)
            showSuccessDialog = false
            navController.popBackStack()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Informe") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        val exito = viewModel.guardarInforme(
                            curso = curso,
                            anio = anio,
                            semestre = semestre,
                            fecha = fecha,
                            comentarios = comentarios,
                            archivos = archivos
                        )
                        
                        if (exito) {
                            showSuccessDialog = true
                        }
                    }
                },
                enabled = curso.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Icon(Icons.Default.Save, contentDescription = "Guardar")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo de curso
            OutlinedTextField(
                value = curso,
                onValueChange = { curso = it },
                label = { Text("Curso") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.School, contentDescription = "Curso")
                },
                singleLine = true
            )
            
            // Selector de año
            var showAnioDialog by remember { mutableStateOf(false) }
            val anios = (2020..2030).toList()
            
            OutlinedButton(
                onClick = { showAnioDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Año: $anio")
            }
            
            if (showAnioDialog) {
                AlertDialog(
                    onDismissRequest = { showAnioDialog = false },
                    title = { Text("Seleccionar Año") },
                    text = {
                        Column {
                            anios.chunked(3).forEach { rowAnios ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    rowAnios.forEach { anioOption ->
                                        TextButton(
                                            onClick = {
                                                anio = anioOption
                                                showAnioDialog = false
                                            }
                                        ) {
                                            Text(
                                                text = anioOption.toString(),
                                                fontWeight = if (anio == anioOption) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showAnioDialog = false }) {
                            Text("Cerrar")
                        }
                    }
                )
            }
            
            // Selector de semestre
            Text("Semestre:", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(1, 2).forEach { semestreOption ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (semestre == semestreOption),
                                onClick = { semestre = semestreOption }
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (semestre == semestreOption),
                            onClick = { semestre = semestreOption }
                        )
                        Text("Semestre $semestreOption")
                    }
                }
            }
            
            // Selector de fecha
            OutlinedButton(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Fecha")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fecha: $fechaFormateada")
            }
            
            // Campo de comentarios
            OutlinedTextField(
                value = comentarios,
                onValueChange = { comentarios = it },
                label = { Text("Comentarios") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                leadingIcon = {
                    Icon(Icons.Default.Notes, contentDescription = "Comentarios")
                },
                maxLines = 5
            )
            
            // Archivos adjuntos
            Text("Archivos adjuntos:", fontWeight = FontWeight.Bold)
            
            if (archivos.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    archivos.forEachIndexed { index, uri ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.AttachFile,
                                contentDescription = "Archivo ${index + 1}"
                            )
                            Text(
                                uri.lastPathSegment ?: "Archivo ${index + 1}",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            IconButton(
                                onClick = {
                                    archivos = archivos.toMutableList().apply {
                                        removeAt(index)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Close, "Eliminar")
                            }
                        }
                    }
                }
            }
            
            Button(
                onClick = { filePicker.launch("*/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AttachFile, contentDescription = "Adjuntar archivo")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar archivo")
            }
            
            // Mostrar errores
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        
        // Diálogo de selección de fecha
        MaterialDialog(
            dialogState = datePickerDialog,
            buttons = {
                positiveButton(text = "Aceptar")
                negativeButton(text = "Cancelar")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Seleccionar fecha",
                allowedDateValidator = { true }
            ) { date ->
                val calendar = Calendar.getInstance()
                calendar.set(date.year, date.monthValue - 1, date.dayOfMonth)
                fecha = Timestamp(calendar.time)
            }
        }
        
        // Diálogo de éxito
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("¡Éxito!") },
                text = { Text("El informe se ha guardado correctamente") },
                confirmButton = {}
            )
        }
    }
}
